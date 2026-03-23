package com.example.lirikid.activity

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lirikid.R
import com.example.lirikid.model.Song
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailLirik : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_lirik)

        val song: Song? = intent.getParcelableExtra("song")
        if (song != null) {
            val songTitleTextView = findViewById<TextView>(R.id.tvSongTitle)
            val songLyricsTextView = findViewById<TextView>(R.id.detailSongLyricstv)

            songTitleTextView.text = song.songTitle
            songLyricsTextView.text = song.songLyrics
        }
    }
}


