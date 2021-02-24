package com.sprout.ui.main.home.adapter

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.sprout.R
import com.sprout.api.URLConstant
import com.sprout.databinding.AdapterCitytrendItemBinding
import com.sprout.ui.main.home.bean.LZTrendsData

class TrendsAdapter:BaseQuickAdapter<LZTrendsData,
        BaseDataBindingHolder<AdapterCitytrendItemBinding>>
    (R.layout.adapter_citytrend_item) {

    override fun convert(
        holder: BaseDataBindingHolder<AdapterCitytrendItemBinding>,
        item: LZTrendsData
    ) {
        val vh = holder.dataBinding
        if (vh != null) {
            vh.cityTrendsData = item

            Glide.with(context).load(R.mipmap.ic_user_head)
                .apply(RequestOptions.circleCropTransform())
                .into(vh.imgHeader)

            //动态数据资源的图标
            if (item.type == URLConstant.TYPE_IMG) { //数据类型为图片
                if (item.res.size > 1) {
                    vh.imgType.setImageResource(R.mipmap.ic_type_imgs)
                    vh.imgType.visibility = View.VISIBLE
                } else {
                    vh.imgType.visibility = View.GONE
                }
            }else if (item.type == URLConstant.TYPE_VIDEO) {
                vh.imgType.setImageResource(R.mipmap.ic_type_play)
                vh.imgType.visibility = View.VISIBLE

                Glide.with(context).setDefaultRequestOptions(
                    RequestOptions()
                        .frame(1000000)
                        .centerCrop()
                        .error(R.mipmap.ic_video_error)
                )
                    .load(item.url)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                    .into(vh.imgCity)
            }
        }
    }
}
