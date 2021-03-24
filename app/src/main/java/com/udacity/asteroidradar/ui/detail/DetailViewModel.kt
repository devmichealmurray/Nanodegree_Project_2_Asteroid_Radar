package com.udacity.asteroidradar.ui.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.data.model.domain.Asteroid
import com.udacity.asteroidradar.ui.mainactivity.MainActivityViewModel
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : MainActivityViewModel(application) {

    private val _returnAsteroid by lazy { MutableLiveData<Asteroid>() }
    val returnAsteroid: LiveData<Asteroid> get() = _returnAsteroid

    fun getAsteroid(id: Long) {
        getAsteroidFromDb(id)
    }

    private fun getAsteroidFromDb(id: Long) {
        viewModelScope.launch {
            val entity = repository.getAsteroidById(id)
            _returnAsteroid.value = entity.toAsteroid()

        }
    }
}