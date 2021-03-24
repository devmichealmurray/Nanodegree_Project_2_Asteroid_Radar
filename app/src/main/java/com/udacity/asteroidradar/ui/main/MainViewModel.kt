package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.data.model.domain.Asteroid
import com.udacity.asteroidradar.data.model.domain.PictureOfDay
import com.udacity.asteroidradar.data.model.entity.AsteroidEntity
import com.udacity.asteroidradar.ui.mainactivity.MainActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

private const val TAG = "** MainViewModel **"

class MainViewModel(application: Application) : MainActivityViewModel(application) {

    private val todayMillis = System.currentTimeMillis()

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>> get() = _asteroidList

    private val _pictureOfDay by lazy { MutableLiveData<PictureOfDay>() }
    val pictureOfDay: LiveData<PictureOfDay> get() = _pictureOfDay

    private val _mainFragmentError by lazy { MutableLiveData<String>() }
    val mainFragmentError: LiveData<String> get() = _mainFragmentError


    /**
     *  Database Functions
     */

    fun getAsteroidsFromDb() {
        val updatedList = ArrayList<AsteroidEntity>()
        viewModelScope.launch {
            try {
                repository.getAsteroids()
                    .flowOn(Dispatchers.IO)
                    .collect { asteroidList ->
                        /**
                         *  Conditional removes old asteroids from database and sends current
                         *  data to prepareAsteroidList to be mapped and sorted
                         */
                        asteroidList.forEach {
                            if (it.dateMillis < todayMillis) {
                                deleteAsteroid(it.uid)
                            } else {
                                updatedList.add(it)
                            }
                        }
                        prepareAsteroidList(updatedList)
                    }
            } catch (e: Exception) {
                _mainFragmentError.value = e.message.toString()
            }
        }
    }

    fun getPhotoFromDb() {
        viewModelScope.launch {
            try {
                repository.getPicture()
                    .flowOn(Dispatchers.IO)
                    .collect {
                        _pictureOfDay.value = it.toPictureOfDay()
                    }

            } catch (e: Exception) {
                _mainFragmentError.value = "GetPhotoFromDB: ${e.message.toString()}"
            }
        }
    }


    private fun deleteAsteroid(asteroidId: Long) {
        viewModelScope.launch {
            repository.deleteAsteroidById(asteroidId)
        }
    }


    private fun prepareAsteroidList(list: ArrayList<AsteroidEntity>) {
        _asteroidList.value = list.map { it.toAsteroid() }.sortedBy { it.date }
    }

}