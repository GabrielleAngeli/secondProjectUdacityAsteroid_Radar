package com.udacity.asteroidradar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding

/**
 * @param asteroidList pass in list generated with items
 */
class AsteroidAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Asteroid, AsteroidAdapter.AsteroidViewHolder>(DiffCallback()) {

    /**
     * ViewHolder class, constructor creates View object
     */
    class AsteroidViewHolder(private val binding: ItemAsteroidBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Puts data into the views
         * @param Asteroid object
         */
        fun bind(asteroid: Asteroid) {
            binding.data = asteroid
            binding.executePendingBindings()
        }
    }

    /**
     * Creates a new ViewHolder with AsteroidViewHolder as template, this function is only called initially
     * This is a required method RecyclerView.Adapter class
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        // LayoutInflater class transforms xml files into view objects
        // This code is always written the same
        val binding =
            ItemAsteroidBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AsteroidViewHolder(binding)
    }

    /**
     * This binds existing views with new data, this function is called multiple times
     * This is a required method RecyclerView.Adapter class
     */
    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(currentItem)
        }
        holder.bind(currentItem)
    }

    /**
     * DiffUtil is a utility class that calculates
     * the difference between two lists
     * and outputs a list of update operations
     * that converts the first list into the second one
     */
    class DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }

    class OnClickListener(val onClickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = onClickListener(asteroid)
    }
}

