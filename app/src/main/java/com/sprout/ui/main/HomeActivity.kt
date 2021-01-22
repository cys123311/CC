package com.sprout.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout
import com.sprout.R
import com.sprout.ui.main.addition.AdditionFragment
import com.sprout.ui.main.discover.DiscoverFragment
import com.sprout.ui.main.home.HomeFragment
import com.sprout.ui.main.message.MessageFragment
import com.sprout.ui.main.mine.MineFragment
import com.sprout.widget.clicks
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() ,View.OnClickListener {

    private var manager: FragmentManager? = null

    private val homeFrag = HomeFragment()//首页
    private val discover = DiscoverFragment()//发现
    private val addition = AdditionFragment()//添加
    private val message = MessageFragment()//消息
    private val mine = MineFragment()//我的

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initView()
        initFragment()
        initListener()
    }

    private fun initView() {
        val intent = intent
        if (intent.hasExtra("pos")) {
            val pos  = intent.getIntExtra("pos",0)
            upTabFragment(pos)
        }
    }

    //TODO fragment管理器
    private fun initFragment() {
        manager = supportFragmentManager
        val t = manager!!.beginTransaction()

        t.add(R.id.mFrame, homeFrag).add(R.id.mFrame, discover)
            .add(R.id.mFrame, addition).add(R.id.mFrame, message).add(R.id.mFrame, mine)
            .hide(discover).hide(addition)
            .hide(message).hide(mine).commit()
    }
    //TODO 点击事件
    private fun initListener() {
        layout_home.setOnClickListener(this)
        layout_search.setOnClickListener(this)
        layout_issue.setOnClickListener(this)
        layout_info.setOnClickListener(this)
        layout_me.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        //开启事物
        val t = manager!!.beginTransaction()
        resetImg()//重置所有属性
        when (v!!.id) {
            R.id.layout_home,0 -> {
                iv_oasis_home_img.setImageResource(R.mipmap.main_nav_home_hl)
                t.show(homeFrag).hide(discover).hide(addition).hide(message).hide(mine)
            }
            R.id.layout_search,1 -> {
                iv_oasis_search_img.setImageResource(R.mipmap.main_nav_discover_hl)
                t.show(discover).hide(homeFrag).hide(addition).hide(message).hide(mine)
            }
            R.id.layout_issue,2 -> {//点击加号跳转
                t.show(addition).hide(homeFrag).hide(discover).hide(message).hide(mine)
            }
            R.id.layout_info,3 -> {
                iv_oasis_info_img.setImageResource(R.mipmap.main_nav_message_hl)
                t.show(message).hide(homeFrag).hide(addition).hide(discover).hide(mine)

            }
            R.id.layout_me,4 -> {
                iv_oasis_me_img.setImageResource(R.mipmap.main_nav_mine_hl)
                t.show(mine).hide(homeFrag).hide(addition).hide(discover).hide(message)
            }
        }
        t.commit()
    }

    //重置所有属性
    private fun resetImg() {
        iv_oasis_home_img.setImageResource(R.mipmap.main_nav_home_normal)
        iv_oasis_search_img.setImageResource(R.mipmap.main_nav_discover_normal)
        iv_oasis_info_img.setImageResource(R.mipmap.main_nav_message_normal)
        iv_oasis_me_img.setImageResource(R.mipmap.main_nav_mine_normal)
    }

    //更改 Tab 显示 相关Fragment
    private fun upTabFragment(pos: Int) {

    }
}