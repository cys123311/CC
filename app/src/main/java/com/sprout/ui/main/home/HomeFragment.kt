package com.sprout.ui.main.home

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.sprout.R
import com.sprout.base.BaseFragment
import com.sprout.databinding.FragmentHomeBinding
import com.sprout.ui.main.home.fragment.CityFragment
import com.sprout.ui.main.home.fragment.ConcernFragment
import com.sprout.ui.main.home.fragment.RecommendFragment


class HomeFragment : BaseFragment<HomeFraViewModel,FragmentHomeBinding>() {

    val fragment = ArrayList<Fragment>()
    val title =ArrayList<String>()
    val city = CityFragment() //同城
    val concern = ConcernFragment() //关注
    val recommend = RecommendFragment() //推荐

    override fun initView() {
        if(fragment.size==0){
            fragment.add(city)
            fragment.add(concern)
            fragment.add(recommend)
            title.add("同城")
            title.add("关注")
            title.add("推荐")
        }
    }

    override fun initVM() {
        v.mVpHome.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return fragment[position]
            }

            override fun getCount(): Int {
                return fragment.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return title[position]
            }
        }
        v.mTabHome.setupWithViewPager(v.mVpHome)
        initClicks(v.mTabHome)
    }

    override fun initData() {

    }

    private fun initClicks(mTabHome: TabLayout) {
        //在这里插入代码片
        mTabHome.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab) {

                    val view  = tab.customView as TextView
                    // 改变 tab 未选择状态下的字体大小
                    view.textSize = 18F
                    // 改变 tab 未选择状态下的字体颜色
                    context?.let { ContextCompat.getColor(it, R.color.home_tab_unselected) }?.let {
                        view.setTextColor(
                            it
                        ) }
            }
            override fun onTabSelected(tab: TabLayout.Tab) {

                    // 获取 tab 组件
                    val view = tab as TextView
                    // 改变 tab 选择状态下的字体大小
                    view.textSize = 24F
                    // 改变 tab 选择状态下的字体颜色
                    context?.let { ContextCompat.getColor(it, R.color.colorB) }?.let {
                        view.setTextColor(
                            it
                        )
                    }
                }
        })
    }

    override fun initClick() {

    }

    override fun lazyLoadData() {

    }


}