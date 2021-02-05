package com.sprout.ui.main.home.bean

class LZTrendsData(
    val address: String, // 南京
    val avater: String,
    val channelid: Int,
    val date: String,    // 2021-02-3 07:50:39
    val goods: Int,      //0
    val id: Int,         //65
    val lat: Int,
    val lng: Int,
    val mood: String,       //今天好心情 好天气
    val nickname: String,
    val res: List<Re>,      //标签
    val themeid: Int,
    val title: String,   //好天气
    val type: Int,       //1
    val uid: String,     // 4c6aca29-1b73-4a22-b507-04e0e3208f4e
    val url: String      //图片 1.png
){

data class Re(
    val tags: List<Tags>,
    val url: String
)

    //标签
data class Tags(
    val tagtype: Int,
    val tagid: Int,
    val tagname: String,
    val tag_x: Int,
    val tag_y: Int,
    var resid: Int,
    val lng: Int,
    val lat: Int
)}