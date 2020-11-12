package com.example.jaimequeraltgarrigos.kotlinmovieapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jaimequeraltgarrigos.kotlinmovieapp.R
import com.example.jaimequeraltgarrigos.kotlinmovieapp.model.MovieEntity
import com.example.jaimequeraltgarrigos.kotlinmovieapp.network.BASE_IMAGE_URL
import kotlinx.android.synthetic.main.cardview_movies.view.*

class MovieAdapter : ListAdapter<MovieEntity, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = getItem(position)
        (holder as MovieViewHolder).bind(movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_movies, parent, false)
        return MovieViewHolder(view)
    }

    class MovieViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: MovieEntity) {
            itemView.TitleTV.text = item.title
            Glide
                .with(itemView.context)
                .load(BASE_IMAGE_URL + item.backdrop_path)
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .into(itemView.posterIV);
        }
    }
}

private class MovieDiffCallback : DiffUtil.ItemCallback<MovieEntity>() {

    override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
        return oldItem == newItem
    }
}