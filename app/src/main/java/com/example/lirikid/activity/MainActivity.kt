package com.example.lirikid.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.lirikid.R
import com.example.lirikid.adapter.KategoriAdapter
import com.example.lirikid.adapter.SectionSongListAdapter
import com.example.lirikid.adapter.SongAdapter
import com.example.lirikid.databinding.ActivityMainBinding
import com.example.lirikid.model.KategoriModel
import com.example.lirikid.model.Song
import com.example.lirikid.model.SongModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var songAdapter: SongAdapter
    private lateinit var songList: MutableList<Song>
    private lateinit var searchView: SearchView
    private lateinit var binding: ActivityMainBinding
    private lateinit var kategoriAdapter: KategoriAdapter
    private lateinit var sectionSongListAdapter: SectionSongListAdapter
    private lateinit var allSongs: List<SongModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getKategoris()

        setupSongRecyclerView()
        setupSearchView()
        setupSection(
            "section1",
            binding.section1MainLayout,
            binding.section1Title,
            binding.section1RecyclerView
        )
        binding.optionBtn.setOnClickListener{
            showPopupMenu()

        }
    }
    fun showPopupMenu(){
        val popupMenu = PopupMenu(this,binding.optionBtn)
        val inflator = popupMenu.menuInflater
        inflator.inflate(R.menu.option_menu,popupMenu.menu)
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.logout ->{
                    logout()
                    true
                }
            }
            false
        }

    }
    fun logout(){
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
    override fun onResume() {
        super.onResume()
        showPlayerView()
    }

    fun showPlayerView() {
        binding.playerView.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java))
        }
        MyExoplayer.getCurrentSong()?.let {
            binding.playerView.visibility = View.VISIBLE
            binding.songTitleTextView.text = "Sedang Diputar :" + it.title
            Glide.with(binding.songCoverImageView).load(it.coverUrl)
                .apply(
                    RequestOptions().transform(RoundedCorners(32))
                ).into(binding.songCoverImageView)
        } ?: run {
            binding.playerView.visibility = View.GONE
        }
    }

    private fun setupSongRecyclerView() {
        songList = mutableListOf()
        recyclerView = binding.recyclerView
        songAdapter = SongAdapter(songList)
        recyclerView.adapter = songAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchSongs()
    }

    private fun setupSearchView() {
        searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun getKategoris() {
        FirebaseFirestore.getInstance().collection("kategori")
            .get()
            .addOnSuccessListener { result ->
                val kategoriList = result.toObjects(KategoriModel::class.java)
                setupKategoriRecyclerView(kategoriList)
            }
            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting categories.", exception)
                Toast.makeText(this, "Error loading categories", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupKategoriRecyclerView(kategoriList: List<KategoriModel>) {
        kategoriAdapter = KategoriAdapter(kategoriList)
        binding.kategoriRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.kategoriRecyclerView.adapter = kategoriAdapter
    }

    fun setupSection(
        id: String,
        mainLayout: RelativeLayout,
        textView: TextView,
        recyclerView: RecyclerView
    ) {
        FirebaseFirestore.getInstance().collection("sections")
            .document("Semua Lagu")
            .get().addOnSuccessListener { documentSnapshot ->
                val section = documentSnapshot.toObject(KategoriModel::class.java)
                section?.apply {
                    binding.section1Title.text = Nama
                    fetchSectionSongs(lagus)
                    binding.section1MainLayout.setOnClickListener {
                        SongsListActivity.kategori = section
                        startActivity(Intent(this@MainActivity, SongsListActivity::class.java))
                    }
                }
            }
    }

    private fun fetchSectionSongs(songIds: List<String>) {
        FirebaseFirestore.getInstance().collection("lagus")
            .whereIn("id", songIds)
            .get()
            .addOnSuccessListener { result ->
                allSongs = result.toObjects(SongModel::class.java)
                sectionSongListAdapter = SectionSongListAdapter(allSongs)
                binding.section1RecyclerView.layoutManager = LinearLayoutManager(
                    this@MainActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                binding.section1RecyclerView.adapter = sectionSongListAdapter
            }
            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting songs.", exception)
                Toast.makeText(this, "Error loading songs", Toast.LENGTH_SHORT).show()
            }
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = allSongs.filter { it.title.contains(query, ignoreCase = true) }
            sectionSongListAdapter.filterList(filteredList)
        }
    }

    private fun fetchSongs() {
        val db = Firebase.firestore
        db.collection("songs")
            .get()
            .addOnSuccessListener { result ->
                songList.clear()
                for (document in result) {
                    val song = document.toObject(Song::class.java).apply {
                        songLyrics = songLyrics.replace("_", "\n")
                    }
                    Log.d("MainActivity", song.toString())
                    songList.add(song)
                }
                songAdapter.notifyDataSetChanged()
                songAdapter.onItemClick = { song ->
                    val intent = Intent(this, DetailLirik::class.java)
                    intent.putExtra("song", song)
                    startActivity(intent)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting documents.", exception)
                Toast.makeText(this, "Error loading songs", Toast.LENGTH_SHORT).show()
            }
    }
}
