package com.example.lirikid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lirikid.R
import com.example.lirikid.model.Song

class SongAdapter(var songList: List<Song>) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    var onItemClick: ((Song) -> Unit)? = null

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songTitle: TextView = itemView.findViewById(R.id.tvSongTitle)
        val artistName: TextView = itemView.findViewById(R.id.tvArtistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val currentItem = songList[position]
        holder.songTitle.text = currentItem.songTitle
        holder.artistName.text = currentItem.artistName

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentItem)
        }
}
override fun getItemCount() = songList.size
}
