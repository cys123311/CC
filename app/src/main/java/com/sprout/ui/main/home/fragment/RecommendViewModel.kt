package com.sprout.ui.main.home.fragment

import androidx.fragment.app.Fragment
import com.sprout.base.BaseViewModel

class RecommendViewModel :BaseViewModel() {

    val tapTitles = arrayListOf<String>(
        "精选",
        "明星",
        "美食",
        "时尚",
        "旅行",
        "美妆",
        "运动",
        "家居",
        "摄影",
        "二次元",
        "汽车",
        "艺术",
        "萌宠",
        "颜值",
        "探索",
        "萌娃"
    )

    var fragments = arrayListOf<Fragment>()

    fun fragmentInit(){
        for (i in 0 until tapTitles.size) {
            fragments.add(SubRecommendFragment())
        }
    }
}