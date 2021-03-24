package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.data.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.data.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.api.parsePicture
import com.udacity.asteroidradar.data.model.domain.Asteroid
import com.udacity.asteroidradar.data.model.domain.PictureOfDay
import com.udacity.asteroidradar.data.model.domain.RoomDatabaseClient
import com.udacity.asteroidradar.data.model.entity.AsteroidEntity
import com.udacity.asteroidradar.data.model.entity.PictureOfDayEntity
import com.udacity.asteroidradar.data.repository.DatabaseRepository
import com.udacity.asteroidradar.data.repository.NasaApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "** MainViewModel **"

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var networkAttempts = 0
    private val repository: DatabaseRepository

    init {
        val asteroidDao = RoomDatabaseClient.getDbInstance(application).asteroidDao()
        val pictureDao = RoomDatabaseClient.getDbInstance(application).pictureOfDayDao()
        repository = DatabaseRepository(asteroidDao, pictureDao)
    }


    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>> get() = _asteroidList

    private val _pictureOfDay by lazy { MutableLiveData<PictureOfDay>() }
    val pictureOfDay: LiveData<PictureOfDay> get() = _pictureOfDay

    private val _networkCallFailed by lazy { MutableLiveData<String>() }
    val networkCallFailed: LiveData<String> get() = _networkCallFailed

    private val _error by lazy { MutableLiveData<String>() }
    val error: LiveData<String> get() = _error

    /**
     *  Network Calls
     */


    private fun attemptNetworkCall() {
        viewModelScope.launch {
            delay(2000)
            getNeows()
        }
    }

    fun getNeows() {
        val dates = getNextSevenDaysFormattedDates()
        val startDate = dates.first()
        val endDate = dates.last()
        nasaNetworkCall(startDate, endDate)
    }

    private fun nasaNetworkCall(startDate: String, endDate: String) {
        viewModelScope.launch {
            try {
                NasaApiRepository.getAsteroids(startDate, endDate)
                    .enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            val jsonObject = response.body()?.let { JSONObject(it) }
                            val jsonResults = jsonObject?.let { parseAsteroidsJsonResult(it) }
                            jsonResults?.forEach { addAsteroidToDb(it) }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            networkAttempts++
                            _networkCallFailed.value =
                                "Network call To NASA Number Failed. Making new attempt, now"
                            if (networkAttempts < 3) {
                                attemptNetworkCall()
                            } else {
                                _networkCallFailed.value =
                                    "Repeated network calls have failed. Please try again, later"
                            }
                        }
                    })

            } catch (e: Exception) {
                _error.value = e.message.toString()
            }
        }
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                val response = NasaApiRepository.getPictureOfTheDay()
                if (response.isSuccessful) {
                    val picture = parsePicture(response)
                    addPictureToDb(picture)
                } else {
                    _error.value = response.errorBody().toString()
                }
            } catch (e: Exception) {
                _error.value = e.message.toString()
            }
        }
    }


    /**
     *  Database Functions
     */

    private fun addAsteroidToDb(asteroid: AsteroidEntity) {
        viewModelScope.launch {
            try {
                repository.addAsteroid(asteroid)
            } catch (e: Exception) {
                _error.value = e.message.toString()
            }
        }
    }

    fun getAsteroidsFromDb() {
        viewModelScope.launch {
            try {
                repository.getAsteroids()
                    .flowOn(Dispatchers.IO)
                    .collect { asteroidEntity ->
                        val asteroids = asteroidEntity.map { it.toAsteroid() }
                        _asteroidList.value = asteroids
                    }
            } catch (e: Exception) {
                _error.value = e.message.toString()
            }
        }
    }


    fun deleteOldPhotoFromDb() {
        viewModelScope.launch {
            try {
                repository.deletePicture()
                getPictureOfDay()
            } catch (e: Exception) {
                _error.value = e.message.toString()
            }
        }
    }


    private fun addPictureToDb(photo: PictureOfDayEntity) {
        viewModelScope.launch {
            try {
                repository.addPicture(photo)
                getPhotoFromDb()
            } catch (e: Exception) {
                _error.value = e.message.toString()
            }
        }
    }

    fun getPhotoFromDb() {
        viewModelScope.launch {
            try {
                val photoEntity = repository.getPicture()
                    .flowOn(Dispatchers.IO)
                    .collect {
                        _pictureOfDay.value = it.toPictureOfDay()
                    }

            } catch (e: Exception) {
                _error.value = e.message.toString()
            }
        }
    }


}