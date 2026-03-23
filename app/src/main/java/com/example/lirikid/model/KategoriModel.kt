package com.example.lirikid.model

data class KategoriModel (
    val Nama : String,
    val coverUrl : String,
    val lagus : List<String>
){
    constructor() : this("","", listOf())
}