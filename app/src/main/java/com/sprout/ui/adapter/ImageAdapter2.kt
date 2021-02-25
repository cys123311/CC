package com.sprout.ui.adapter

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.lxj.easyadapter.EasyAdapter
import com.lxj.easyadapter.ViewHolder
import com.lxj.xpopup.XPopup
import com.sprout.R
import com.sprout.utils.XPopupUtils

class ImageAdapter2(val list: List<String>, private val viewPager2: ViewPager2) :
    EasyAdapter<String>(list, R.layout.adapter_image2) {

    override fun bind(holder: ViewHolder, t: String, position: Int) {

        val imageView = holder.getView<ImageView>(R.id.image2)
        //1. 加载图片
        Glide.with(imageView).load(t).into(imageView)

        //2. 设置点击
        imageView.setOnClickListener {
            XPopup.Builder(holder.itemView.context).asImageViewer(
                imageView, position, list, { popupView, position ->
                    viewPager2.setCurrentItem(position, false)
                    //一定要post，因为setCurrentItem内部实现是RecyclerView.scrollTo()，这个是异步的
                    viewPager2.post(Runnable { //由于ViewPager2内部是包裹了一个RecyclerView，而RecyclerView始终维护一个子View
                        val rv = viewPager2.getChildAt(0) as RecyclerView
                        //再拿子View，就是ImageView
                        popupView.updateSrcView(rv.getChildAt(0) as ImageView)
                    })
                }, XPopupUtils.ImageLoader()
            ).show()
        }
    }
}