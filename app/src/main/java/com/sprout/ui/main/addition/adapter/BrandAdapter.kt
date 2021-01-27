package com.sprout.ui.main.addition.adapter

import android.app.Activity
import android.content.Context
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.sprout.BR
import com.sprout.R
import com.sprout.base.BaseAdapter
import com.sprout.databinding.AdapterBrandItemBinding
import com.sprout.ui.main.addition.SubmitMoreActivity
import com.sprout.ui.main.addition.bean.BrandData
import com.zhpan.bannerview.BaseViewHolder

class BrandAdapter :
    BaseQuickAdapter<BrandData.Data,
            BaseDataBindingHolder<AdapterBrandItemBinding>>
        (R.layout.adapter_brand_item){

    override fun convert(
        holder: BaseDataBindingHolder<AdapterBrandItemBinding>,
        item: BrandData.Data
    ) {
        val  brandItemBinding = holder.dataBinding
        if (brandItemBinding!=null){
            brandItemBinding.brandData = item
        }
    }
}