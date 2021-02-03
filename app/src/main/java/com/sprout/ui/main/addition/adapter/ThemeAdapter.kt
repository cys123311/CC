package com.sprout.ui.main.addition.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.sprout.R
import com.sprout.databinding.AdapterThemeItemBinding
import com.sprout.ui.main.addition.bean.LZThemeBean

class ThemeAdapter :
    BaseQuickAdapter<LZThemeBean,
            BaseDataBindingHolder<AdapterThemeItemBinding>>
        (R.layout.adapter_theme_item){

    override fun convert(
        holder: BaseDataBindingHolder<AdapterThemeItemBinding>,
        item: LZThemeBean
    ) {
        holder.dataBinding!!.theme = item
    }
}