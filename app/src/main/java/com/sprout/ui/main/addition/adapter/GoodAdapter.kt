package com.sprout.ui.main.addition.adapter

import android.app.Activity
import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.sprout.R
import com.sprout.base.BaseAdapter
import com.sprout.databinding.AdapterBrandItemBinding
import com.sprout.databinding.AdapterGoodItemBinding
import com.sprout.ui.main.addition.bean.BrandData
import com.sprout.ui.main.addition.bean.GoodData

class GoodAdapter : BaseQuickAdapter<GoodData.Data,
        BaseDataBindingHolder<AdapterGoodItemBinding>>
(R.layout.adapter_good_item){

    override fun convert(
        holder: BaseDataBindingHolder<AdapterGoodItemBinding>,
        item: GoodData.Data
    ) {
        val goodItemBinding = holder.dataBinding
        if (goodItemBinding!=null)
            goodItemBinding.goodData = item
    }
}