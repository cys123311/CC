package com.sprout.ui.main.home.adapter

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.sprout.BR
import com.sprout.R
import com.sprout.databinding.AdapterSubrecItemBinding
import com.sprout.ui.main.home.bean.LZTrendsData

class SubRecAdapter :
    BaseQuickAdapter<LZTrendsData,BaseDataBindingHolder<AdapterSubrecItemBinding>>
    (R.layout.adapter_subrec_item){

    override fun convert(
        holder: BaseDataBindingHolder<AdapterSubrecItemBinding>,
        item: LZTrendsData
    ) {
        val imgType = holder.getView<ImageView>(R.id.img_type)
        imgType.visibility = View.GONE
        if(item.url.endsWith(".png") ||
            item.url.endsWith(".jpg") ||
            item.url.endsWith(".gif")){

            if(item.res.size > 1){
                imgType.setImageResource(R.mipmap.icon_photos)
                imgType.visibility = View.VISIBLE
            }
        }else if(item.url.endsWith(".mp4")){
            imgType.setImageResource(R.mipmap.icon_video)
            imgType.visibility = View.VISIBLE
        }
        holder.dataBinding!!.trendsData = item
    }

}