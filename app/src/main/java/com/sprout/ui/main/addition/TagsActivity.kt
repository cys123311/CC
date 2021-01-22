package com.sprout.ui.main.addition

import android.util.SparseArray
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sprout.BR
import com.sprout.R
import com.sprout.base.BaseActivity
import com.sprout.databinding.ActivityTagsBinding
import com.sprout.ui.main.addition.adapter.BrandAdapter
import com.sprout.ui.main.addition.adapter.GoodAdapter
import com.sprout.ui.main.addition.bean.BrandData
import com.sprout.ui.main.addition.bean.GoodData
import kotlinx.android.synthetic.main.activity_tags.*

class TagsActivity :
    BaseActivity<TagsActViewModel,ActivityTagsBinding>() {
    override fun initView() {
        TODO("Not yet implemented")
    }

    override fun initClick() {
        TODO("Not yet implemented")
    }

    override fun initData() {
        TODO("Not yet implemented")
    }

    override fun initVM() {
        TODO("Not yet implemented")
    }

    /*lateinit var brandList:MutableList<BrandData.Data>
    lateinit var brandAdapter: BrandAdapter

    lateinit var goodList:MutableList<GoodData.Data>
    lateinit var goodAdapter: GoodAdapter


    override fun initData() {

    }

    override fun initVM() {
         vm.brandList.observe(this, Observer {
             //适配器
             val brandAdapter = BrandAdapter()
             //布局管理器
             v.recyTags.layoutManager = LinearLayoutManager(this)
             //绑定数据
             brandAdapter
             //绑定适配器
             v.recyTags.adapter = brandAdapter
        })

        vm.goodList.observe(this, Observer {
            var goodArr = SparseArray<Int>()
            brandArr.put(R.layout.adapter_brand_item, BR.goodData)
            goodList = mutableListOf()
            goodAdapter = GoodAdapter(this,goodList,goodArr,GoodClick())

            mDataBinding.tagClick = TagsClick()

            goodList.clear()
            goodList.addAll(it.data)
            recyTags.adapter = goodAdapter
        })
    }

    override fun initView() {


    }

    inner class BrandClick:IItemClick<BrandData.Data>{
        override fun itemClick(data: BrandData.Data) {
            intent.putExtra("name",data.name)
            intent.putExtra("id",data.id)
            setResult(1,intent)
            finish()
        }

    }

    inner class GoodClick:IItemClick<GoodData.Data>{
        override fun itemClick(data: GoodData.Data) {
            intent.putExtra("name",data.name)
            intent.putExtra("id",data.id)
            setResult(2,intent)
        }

    }

    inner class TagsClick{
        fun click(type:Int){
            when(type){
                1->{
                    if(brandList.size == 0){
                        mViewModel.getBrand()
                    }else{
                        recyTags.adapter = brandAdapter
                    }
                }
                2->{
                    if(goodList.size == 0){
                        mViewModel.getGood()
                    }else{
                        recyTags.adapter = goodAdapter
                    }
                }
                3->{

                }
                4->{

                }
            }
        }
    }*/
}