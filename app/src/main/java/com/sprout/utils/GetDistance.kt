package com.sprout.utils

import com.sprout.ui.bean.Distribution
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


object GetDistance {

    fun getDistance(start: Distribution,end: Distribution): Double {

        val lon1: Double = Math.PI / 180 * start.longitude
        val lon2: Double = Math.PI / 180 * end.longitude
        val lat1: Double = Math.PI / 180 * start.dimensionality
        val lat2: Double = Math.PI / 180 * end.dimensionality


        // 地球半径
        val R = 6371

        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        return acos(sin(lat1) * sin(lat2) + cos(lat1) * cos(lat2) * cos(lon2 - lon1)) * R
    }

}
