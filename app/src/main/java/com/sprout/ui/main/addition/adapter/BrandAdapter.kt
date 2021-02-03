package com.sprout.ui.main.addition.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.sprout.R
import com.sprout.databinding.AdapterBrandItemBinding
import com.sprout.databinding.AdapterGoodItemBinding
import com.sprout.ui.main.addition.bean.BrandData


class BrandAdapter: BaseQuickAdapter<BrandData.Data,
        BaseDataBindingHolder<AdapterBrandItemBinding>>
    (R.layout.adapter_brand_item){

    override fun convert(
        holder: BaseDataBindingHolder<AdapterBrandItemBinding>,
        item: BrandData.Data
    ) {
        holder.dataBinding!!.brandData =item
    }
}