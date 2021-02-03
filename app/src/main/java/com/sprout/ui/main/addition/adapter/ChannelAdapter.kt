package com.sprout.ui.main.addition.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.sprout.R
import com.sprout.databinding.AdapterChannelItemBinding
import com.sprout.ui.main.addition.bean.LZChannelBean

class ChannelAdapter :
    BaseQuickAdapter<LZChannelBean,
            BaseDataBindingHolder<AdapterChannelItemBinding>>
        (R.layout.adapter_channel_item) {

    override fun convert(
        holder: BaseDataBindingHolder<AdapterChannelItemBinding>,
        item: LZChannelBean
    ) {
        holder.dataBinding!!.channel = item
    }
}