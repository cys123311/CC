package com.sprout.ui.main.addition.bean

import android.text.Editable

data class LZSubmitTrends(
    val address: String,
    val channelid: Int,
    val lat: Int,
    val lng: Int,
    val mood: String,
    val res: List<Re>,
    val themeid: Int,
    val title: Editable,
    val type: Int
) {

    data class Re(
        val tags: List<Tag>,
        val url: String
    )

    data class Tag(
        val id: Int,
        val lat: Int,
        val lng: Int,
        val name: String,
        val type: Int,
        val x: Int,
        val y: Int
    )
}
