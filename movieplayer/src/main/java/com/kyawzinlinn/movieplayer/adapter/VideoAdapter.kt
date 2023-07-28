package com.kyawzinlinn.movieplayer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.kyawzinlinn.movieplayer.databinding.VideoGridItemBinding
import com.kyawzinlinn.movieplayer.databinding.VideoListItemBinding
import com.kyawzinlinn.movieplayer.model.Video
import com.kyawzinlinn.movieplayer.utils.VideoViewType

class VideoAdapter(private val videoViewType: VideoViewType, private val onVideoClick: (Video) -> Unit ): ListAdapter<Video,RecyclerView.ViewHolder>(DiffCallBack) {
    private lateinit var context: Context
    class GridViewHolder(private val binding: VideoGridItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(video: Video, context: Context){
            binding.apply {
                Glide.with(context).load(video.path).into(ivGridMovieThumbnail)
                tvGridVideoName.text = video.title
            }
        }
    }

    class ListViewHolder(private val binding: VideoListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(video: Video, context: Context){
            binding.apply {
                Glide.with(context).load(video.path).into(ivListMovieThumbnail)
                tvListVideoName.text = video.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return when(videoViewType){
            VideoViewType.GRID -> GridViewHolder(VideoGridItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            VideoViewType.LIST -> ListViewHolder(VideoListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = getItem(position)
        holder.itemView.setOnClickListener { onVideoClick(video) }
        when(videoViewType){
            VideoViewType.LIST -> (holder as ListViewHolder).bind(video,context)
            VideoViewType.GRID -> (holder as GridViewHolder).bind(video,context)
        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Video>(){
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.path == newItem.path
        }

    }
}