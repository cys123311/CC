package com.sprout.ui.main.home

import com.luck.picture.lib.tools.ScreenUtils
import com.lxj.xpopup.XPopup
import com.sprout.api.URLConstant
import com.sprout.api.ext.bindViewPager2
import com.sprout.api.ext.init
import com.sprout.api.ext.navigationTo
import com.sprout.base.BaseFragment
import com.sprout.databinding.FragmentHomeBinding
import com.sprout.ui.main.login.RegisterActivity
import com.sprout.utils.MyMmkv
import com.sprout.utils.ToastUtil
import com.sprout.widget.clicks
import org.jetbrains.anko.alert
import org.jetbrains.anko.support.v4.alert


class HomeFragment : BaseFragment<HomeFraViewModel,FragmentHomeBinding>() {

    override fun initView() {
        //初始化fragmentList
        vm.fragmentInit()
        //初始化viewpager2
        v.vp2Home.init(this, fragments = vm.fragments).offscreenPageLimit =
            vm.fragments.size

        //初始化指示器
        v.magicIndicatorHome.bindViewPager2(v.vp2Home, mStringList = vm.tabList)



    }

    override fun initVM() {

    }

    override fun initData() {

    }

    override fun initClick() {
        v.waterDrop.clicks{
//            navigationTo<MusicActivity>()
            //退出登录
            exitLog()
        }
    }

    override fun lazyLoadData() {

    }

    private fun exitLog(){
        val uid = MyMmkv.getString("uid")
        if (!uid.isNullOrEmpty()){
            alert("退出登录"){
                negativeButton("去意已定"){
                    MyMmkv.removeKey(arrayOf("uid", URLConstant.token))
                    ToastUtil.showToast(mContext,"退出登录成功")
                }
                positiveButton("容我想想"){

                }
            }.show()
        }else{
            ToastUtil.showToast(mContext,"已退出登录")
        }
    }
}