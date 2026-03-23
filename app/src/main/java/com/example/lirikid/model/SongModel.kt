package com.example.lirikid.model

data class SongModel (
    val id : String,
    val title : String,
    val Artist : String,
    val songLyrics : String,
    val url : String,
    val coverUrl : String,
){
    constructor() : this("","","","","", "")
}
