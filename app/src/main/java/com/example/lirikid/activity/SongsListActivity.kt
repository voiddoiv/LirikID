package com.example.lirikid.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.lirikid.adapter.SongsListAdapter
import com.example.lirikid.databinding.ActivitySongsListBinding
import com.example.lirikid.model.KategoriModel

class SongsListActivity : AppCompatActivity() {
    companion object {
        lateinit var kategori: KategoriModel
    }

    private lateinit var binding: ActivitySongsListBinding
    private lateinit var songsListAdapter: SongsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nameTextView.text = kategori.Nama
        Glide.with(this).load(kategori.coverUrl)
            .apply(
                RequestOptions().transform(RoundedCorners(32))
            )
            .into(binding.coverImageView)

        setupSongsListRecyclerView()
    }

    private fun setupSongsListRecyclerView() {
        songsListAdapter = SongsListAdapter(kategori.lagus)
        binding.songsListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.songsListRecyclerView.adapter = songsListAdapter
    }
}
