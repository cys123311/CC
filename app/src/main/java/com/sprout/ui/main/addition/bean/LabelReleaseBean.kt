package com.sprout.ui.main.addition.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 封装json 串
 */
@Parcelize
data class LabelReleaseBean(
    val address: String,
    val channelid: Int,
    val lat: Double,
    val lng: Double,
    val mood: String,
    val res: List<Res>,
    val themeid: Int,
    val title: String,
    val type: Int
):Parcelable

@Parcelize
data class Res(
    val tags: List<Tag>?,
    val url: String
):Parcelable

@Parcelize
data class Tag(
    val id: Int,
    val lat: Double,
    val lng: Double,
    val name: String,
    val type: Int,
    val x: Float,
    val y: Float
):Parcelable