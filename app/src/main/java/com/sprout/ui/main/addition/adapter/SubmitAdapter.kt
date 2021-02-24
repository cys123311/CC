package com.sprout.ui.main.addition.adapter

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.sprout.R
import com.sprout.databinding.AdapterSubmitItemBinding
import com.sprout.ui.main.addition.bean.ImgData


class SubmitAdapter :
BaseQuickAdapter<ImgData,BaseDataBindingHolder<AdapterSubmitItemBinding>>
    (R.layout.adapter_submit_item){

    override fun convert(
        holder: BaseDataBindingHolder<AdapterSubmitItemBinding>,
        item: ImgData
    ) {
        val  bind = holder.dataBinding
        if(bind!=null){
            if (item.type==1){ //图片
                if(item.path.isNullOrEmpty()){ //数据为空
                    //添加 图片 标记 隐藏
                    bind.lineSubmitImg.setImageResource(R.drawable.ic_addimg)
                    bind.lineSubmitDelete.visibility = View.GONE
                }
            }else if(item.type==2){ //当前数据是视频 -- 显示视频的第一帧 frame()
                Glide.with(context).setDefaultRequestOptions(
                    RequestOptions()
                        .frame(1000000)
                        .centerCrop()
                        .error(R.mipmap.ic_video_error)
                )
                    .load(item.path)
                    .into(bind.lineSubmitImg)
            }
            bind.submitData = item
        }
    }
}
