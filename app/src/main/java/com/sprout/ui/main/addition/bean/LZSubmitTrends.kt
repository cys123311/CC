package com.sprout.ui.main.addition.bean

import android.text.Editable

data class LZSubmitTrends(
    val `data`: Data,
    val errmsg: String,
    val errno: Int
){

data class Data(
    val result: String
)}