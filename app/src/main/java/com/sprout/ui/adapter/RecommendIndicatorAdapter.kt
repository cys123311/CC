package com.sprout.ui.adapter

import android.content.Context
import androidx.viewpager2.widget.ViewPager2
import com.sprout.App
import com.sprout.R
import com.sprout.api.ext.setOnclick
import com.sprout.widget.viewpager.ScaleTransitionPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView

class RecommendIndicatorAdapter(val list: List<String>,val pager:ViewPager2):CommonNavigatorAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun getTitleView(context: Context?, index: Int): IPagerTitleView {

        return ScaleTransitionPagerTitleView(App.instance).apply {
            text = list[index]
            textSize = 16f
            normalColor = resources.getColor(R.color.tabColor)
            selectedColor = resources.getColor(R.color.main_color)

            setOnclick{ pager.currentItem = index }
        }
    }

    override fun getIndicator(context: Context?): IPagerIndicator? {
        return null
    }
}