package com.udacity.asteroidradar.ui.mainactivity

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.data.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.data.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.api.parsePicture
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

private const val TAG = "MainActivityViewModel"

open class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var networkAttempts = 0

    val repository: DatabaseRepository

    private val _networkCallFailed by lazy { MutableLiveData<String>() }
    val networkCallFailed: LiveData<String> get() = _networkCallFailed

    private val _error by lazy { MutableLiveData<String>() }
    val error: LiveData<String> get() = _error

    init {
        val asteroidDao = RoomDatabaseClient.getDbInstance(application).asteroidDao()
        val pictureDao = RoomDatabaseClient.getDbInstance(application).pictureOfDayDao()
        repository = DatabaseRepository(asteroidDao, pictureDao)
    }


    /**
     *  Database check for initial install; if db is empty, make network call and load data.
     */

    fun initialAsteroidsDownload() {
        viewModelScope.launch {
            repository.getAsteroids()
                .flowOn(Dispatchers.IO)
                .collect { asteroidList ->
                    if (asteroidList.isNullOrEmpty()) {
                        Log.d(TAG, "========== update Asteroids Called ==============")
                        getNeows()
                    }
                }
        }
    }


    fun initialPhotoDownload() {
        viewModelScope.launch {
            repository.getPicture()
                .flowOn(Dispatchers.IO)
                .collect { photo ->
                    if (photo == null) {
                        getPictureOfDay()
                    }
                }
        }
    }


    /**
     *  Network Calls
     */

    fun getNeows() {
        val dates = getNextSevenDaysFormattedDates()
        val startDate = dates.first()
        val endDate = dates.last()
        nasaNetworkCall(startDate, endDate)
    }

    private fun reattemptNetworkCall() {
        viewModelScope.launch {
            delay(2000)
            getNeows()
        }
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
                            _networkCallFailed.value = networkAttempts.toString()
                            if (networkAttempts < 3) {
                                reattemptNetworkCall()
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
                    if (response.body()?.mediaType == "image") {
                        val picture = parsePicture(response)
                        addPictureToDb(picture)
                    } else {
                        _error.value = "No New Photo Today"
                    }
                } else {
                    _error.value = response.errorBody().toString()
                }
            } catch (e: Exception) {
                _error.value = "GetPictureOfDay: ${e.message.toString()}"
            }
        }
    }


    /**
     * Add Data To DB
     */

    fun addAsteroidToDb(asteroid: AsteroidEntity) {
        viewModelScope.launch {
            try {
                repository.addAsteroid(asteroid)
            } catch (e: Exception) {
                _error.value = e.message.toString()
            }
        }
    }

    private fun addPictureToDb(photo: PictureOfDayEntity) {
        viewModelScope.launch {
            try {
                repository.deletePicture()
                repository.addPicture(photo)
            } catch (e: Exception) {
                _error.value = "AddPictureToDB: ${e.message.toString()}"
            }
        }
    }
}