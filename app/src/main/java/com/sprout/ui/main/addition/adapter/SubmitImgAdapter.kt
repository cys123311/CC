package com.sprout.ui.main.addition.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.sprout.R
import com.sprout.databinding.AdapterSubmitImgitemBinding

import com.sprout.ui.main.addition.bean.ImgData
import com.sprout.utils.PicSelectUtils

class SubmitImgAdapter :
BaseQuickAdapter<ImgData,BaseDataBindingHolder<AdapterSubmitImgitemBinding>>
    (R.layout.adapter_submit_imgitem){

    override fun convert(
        holder: BaseDataBindingHolder<AdapterSubmitImgitemBinding>,
        item: ImgData
    ) {
        val  bind = holder.dataBinding
        if(bind!=null){
            if(item.path.isNullOrEmpty()){ //数据为空
                //添加 图片 标记 隐藏
                bind.lineSubmitImg.setImageResource(R.drawable.ic_addimg)
                bind.lineSubmitDelete.visibility = View.GONE
            }else{
                bind.submitData = item
            }

//            bind.lineSubmitImg.setOnClickListener {
//                if(!item.path.isNullOrEmpty())
//                bind.lineSubmitDelete.visibility = if (bind.lineSubmitDelete.visibility == View.GONE) View.VISIBLE else View.GONE
//            }
        }

    }
}
