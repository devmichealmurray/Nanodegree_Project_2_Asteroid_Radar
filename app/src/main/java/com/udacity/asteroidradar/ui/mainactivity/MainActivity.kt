package com.udacity.asteroidradar.ui.mainactivity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.udacity.asteroidradar.R
import org.jetbrains.anko.alert

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.apply {
            initialAsteroidsDownload()
            initialPhotoDownload()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.apply {
            error.observe(this@MainActivity, errorObserver)
            networkCallFailed.observe(this@MainActivity, networkCallObserver)
        }
    }


    /**
     *  Observers that let user know if any errors have occurred
     */

    private val errorObserver = Observer<String> { errorMessage ->
        alert {
            title = getString(R.string.error_alert_title)
            message = errorMessage
            isCancelable = false
            positiveButton(getString(R.string.alert_positive_button)) { dialog ->
                dialog.dismiss()
            }
        }.show()
    }

    private val networkCallObserver = Observer<String> { message ->
        Toast.makeText(
            this,
            "Nasa Network Call Number $message Failed. Reattempting Connection.",
            Toast.LENGTH_LONG
        ).show()
    }


}
