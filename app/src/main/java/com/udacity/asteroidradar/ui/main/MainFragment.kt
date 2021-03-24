package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data.model.domain.Asteroid
import com.udacity.asteroidradar.data.model.domain.PictureOfDay
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.ui.adapter.AsteroidRecyclerView
import org.jetbrains.anko.support.v4.alert

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.apply {
            getAsteroidsFromDb()
            getPhotoFromDb()
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.apply {
            asteroidList.observe(viewLifecycleOwner, asteroidListObserver)
            pictureOfDay.observe(viewLifecycleOwner, pictureOfDayObserver)
            mainFragmentError.observe(viewLifecycleOwner, errorObserver)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }



    /**
     *  Observers -- Main Fragment is only concerned with retrieving data from the db
     */

    private val asteroidListObserver = Observer<List<Asteroid>> { list ->
        if (list.isNotEmpty()) {
            binding.asteroidRecycler.adapter = AsteroidRecyclerView(list)
        }
    }

    private val pictureOfDayObserver = Observer<PictureOfDay> { photo ->
        Picasso.get()
            .load(photo.url)
            .into(binding.activityMainImageOfTheDay)
    }

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

}
