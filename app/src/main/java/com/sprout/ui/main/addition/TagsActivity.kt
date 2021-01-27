package com.sprout.ui.main.addition

import android.util.Log
import android.util.SparseArray
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.sprout.BR
import com.sprout.R
import com.sprout.api.IItemClick
import com.sprout.base.BaseActivity
import com.sprout.databinding.ActivityTagsBinding
import com.sprout.ui.main.addition.adapter.BrandAdapter
import com.sprout.ui.main.addition.adapter.GoodAdapter
import com.sprout.ui.main.addition.bean.BrandData
import com.sprout.ui.main.addition.bean.GoodData
import com.sprout.widget.clicks
import kotlinx.android.synthetic.main.activity_tags.*


class TagsActivity :
    BaseActivity<TagsActViewModel, ActivityTagsBinding>(), OnItemChildClickListener {

    lateinit var brandList: MutableList<BrandData.Data>
    val brandAdapter  : BrandAdapter by lazy { BrandAdapter() }

    lateinit var goodList: MutableList<GoodData.Data>
    val goodAdapter  : GoodAdapter by lazy { GoodAdapter() }

    override fun initView() {
        v.recyTags.layoutManager = LinearLayoutManager(this)

        brandList = mutableListOf()
        brandAdapter.setNewInstance(brandList)
        brandAdapter.addChildClickViewIds(R.id.init_brand)
        brandAdapter.setOnItemChildClickListener(this)

        goodList = mutableListOf()
        goodAdapter.setNewInstance(goodList)
        goodAdapter.addChildClickViewIds(R.id.init_good)
        goodAdapter.setOnItemChildClickListener(this)
    }

    override fun initClick() {
        v.imgBrand.clicks{
            if(brandList.size == 0){
                vm.getBrandList(true)
            }else{
                v.recyTags.adapter=brandAdapter
            }
        }
        v.imgGood.clicks {
            if(goodList.size == 0){
                vm.getGoodList(true)
            }else{
                v.recyTags.adapter=goodAdapter
            }
        }
        v.imgUser.clicks {

        }
        v.imgAddress.clicks {

        }
    }

    override fun initData() {

    }

    override fun initVM() {
        vm.brandList.observe(this, Observer {
            brandList.clear()
            brandList.addAll(it.data)
            recyTags.adapter = brandAdapter
        })

        vm.goodList.observe(this, Observer {
            goodList.clear()
            goodList.addAll(it.data)
            recyTags.adapter = goodAdapter
        })
    }

    /**
     * 适配器 条目监听
     */
    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (view.id) {
            R.id.init_brand -> {
                intent.putExtra("name",brandList[position].name)
                intent.putExtra("id",brandList[position].id)
                setResult(1,intent)
                finish()
            }
            R.id.init_good->{
                intent.putExtra("name", goodList[position].name)
                intent.putExtra("id", goodList[position].id)
                setResult(2, intent)
                finish()
            }
            else -> {

            }
        }
    }
}