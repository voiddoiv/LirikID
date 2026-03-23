package com.example.lirikid.model

import android.os.Parcel
import android.os.Parcelable

data class Song(
    val artistName: String = "",
    val songTitle: String = "",
    var songLyrics: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(artistName)
        parcel.writeString(songTitle)
        parcel.writeString(songLyrics)
    }
    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }
        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }
}