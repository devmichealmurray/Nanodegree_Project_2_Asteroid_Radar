package com.udacity.asteroidradar.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.BR
import com.udacity.asteroidradar.data.model.domain.Asteroid
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding


class AsteroidViewHolder(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindAsteroids(asteroid: Asteroid) {
        binding.setVariable(BR.asteroid, asteroid)
        binding.executePendingBindings()
    }
}

class AsteroidRecyclerView(private val list: List<Asteroid>) : RecyclerView.Adapter<AsteroidViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder(ItemAsteroidBinding
            .inflate(LayoutInflater
                .from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        holder.bindAsteroids(list[position])
    }

    override fun getItemCount() = list.size
}