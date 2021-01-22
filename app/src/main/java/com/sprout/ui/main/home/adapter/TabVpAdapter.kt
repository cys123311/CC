package com.sprout.ui.main.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.ArrayList

class TabVpAdapter(
    fm: FragmentManager, var list: ArrayList<Fragment>,
    var title: ArrayList<String>) :
    FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }
}