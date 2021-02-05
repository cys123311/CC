package com.sprout.ui.main.home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.sprout.R
import com.sprout.databinding.AdapterCitytrendItemBinding
import com.sprout.ui.main.home.bean.LZTrendsData

class CityTrendsAdpater
    :BaseQuickAdapter<LZTrendsData,
        BaseDataBindingHolder<AdapterCitytrendItemBinding>>
    (R.layout.adapter_citytrend_item){

    override fun convert(
        holder: BaseDataBindingHolder<AdapterCitytrendItemBinding>,
        item: LZTrendsData
    ) {
        holder.dataBinding!!.cityTrendsData = item
    }

}