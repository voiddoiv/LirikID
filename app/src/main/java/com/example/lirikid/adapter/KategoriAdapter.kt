package com.example.lirikid.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.lirikid.activity.SongsListActivity
import com.example.lirikid.databinding.KategoriItemRViewBinding
import com.example.lirikid.model.KategoriModel
import kotlin.math.log


class KategoriAdapter(private val kategoriList: List<KategoriModel>) :
    RecyclerView.Adapter<KategoriAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: KategoriItemRViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //Bind data with views
        fun bindData(kategori: KategoriModel) {
            binding.nameTextView.text = kategori.Nama
            Glide.with(binding.coverImageView).load(kategori.coverUrl)
                .apply(
                    RequestOptions().transform(RoundedCorners(32))
                )
                .into(binding.coverImageView)
            Log.i("LAGUS",kategori.lagus.size.toString())

            val context = binding.root.context
            binding.root.setOnClickListener{
                SongsListActivity.kategori = kategori
                context.startActivity(Intent(context,SongsListActivity::class.java))
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = KategoriItemRViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return kategoriList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(kategoriList[position])

    }

}


