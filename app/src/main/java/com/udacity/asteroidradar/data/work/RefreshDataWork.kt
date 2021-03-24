package com.udacity.asteroidradar.data.work

import android.app.Application
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.ui.mainactivity.MainActivityViewModel
import java.util.concurrent.TimeoutException

class RefreshDataWork(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            MainActivityViewModel(Application()).getNeows()
            Result.success()
        } catch (e: TimeoutException) {
            Result.retry()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}