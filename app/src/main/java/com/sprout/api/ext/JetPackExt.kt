package com.sprout.api.ext

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.sprout.App
import com.sprout.R
import com.sprout.ui.adapter.RecommendIndicatorAdapter
import com.sprout.ui.main.discover.DiscoverFragment
import com.sprout.ui.main.home.HomeFragment
import com.sprout.ui.main.home.fragment.RecommendFragment
import com.sprout.ui.main.message.MessageFragment
import com.sprout.ui.main.mine.MineFragment
import com.sprout.widget.viewpager.ScaleTransitionPagerTitleView
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.abs.IPagerNavigator
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

/**
 * 初始化MainFragment的ViewPager2
 */
fun ViewPager2.initMain(fragment: Fragment): ViewPager2 {
    //是否可滑动
    this.isUserInputEnabled = false
    this.offscreenPageLimit = 5
    //设置适配器
    adapter = object : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return HomeFragment()
                }
                1 -> {
                    return DiscoverFragment()
                }
                3 -> {
                    return MessageFragment()
                }
                4 -> {
                    return MineFragment()
                }
                else -> {
                    return HomeFragment()
                }
            }
        }
        override fun getItemCount() = 5
    }
    return this
}

//初始化HomeFragment的ViewPager2
fun ViewPager2.init(
    fragment: Fragment,
    fragments: ArrayList<Fragment>,
    isUserInputEnabled: Boolean = true
): ViewPager2 {
    //是否可滑动
    this.isUserInputEnabled = isUserInputEnabled
    //设置适配器
    adapter = object : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int) = fragments[position]
        override fun getItemCount() = fragments.size
    }
    return this
}


//初始化HomeFragment的ViewPager2
fun ViewPager2.initRecommend(
    fragment: Fragment,
    fragments: ArrayList<Fragment>,
    isUserInputEnabled: Boolean = true
): ViewPager2 {
    //是否可滑动
    this.isUserInputEnabled = isUserInputEnabled
    //设置适配器
    adapter = object : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int) = RecommendFragment()
        override fun getItemCount() = fragments.size
    }
    return this
}


fun MagicIndicator.bindViewPager2(
    viewPager: ViewPager2,
    mStringList: List<String> = arrayListOf(),
    action: (index: Int) -> Unit = {}) {
    val commonNavigator = CommonNavigator(App.instance)
    commonNavigator.adapter = object : CommonNavigatorAdapter() {

        override fun getCount(): Int {
            return  mStringList.size
        }
        override fun getTitleView(context: Context, index: Int): IPagerTitleView {
            return ScaleTransitionPagerTitleView(App.instance).apply {
                //设置文本
                text = mStringList[index].toHtml()
                //字体大小
                textSize = 17f


                //未选中颜色
                normalColor = resources.getColor(R.color.tabColor)
                //选中颜色
                selectedColor = resources.getColor(R.color.main_color)
                //点击事件
                setOnClickListener {
                    viewPager.currentItem = index
                    action.invoke(index)
                }
            }
        }
        override fun getIndicator(context: Context): IPagerIndicator {
            return LinePagerIndicator(context).apply {
                mode = LinePagerIndicator.MODE_EXACTLY
                //线条的宽高度
                lineHeight = UIUtil.dip2px(App.instance, 3.0).toFloat()
                lineWidth = UIUtil.dip2px(App.instance, 20.0).toFloat()
                //线条的圆角
                roundRadius = UIUtil.dip2px(App.instance, 6.0).toFloat()
                startInterpolator = AccelerateInterpolator()
                endInterpolator = DecelerateInterpolator(2.0f)
                //线条的颜色
                setColors(resources.getColor(R.color.main_color))
            }
        }
    }
    this.navigator = commonNavigator

    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            this@bindViewPager2.onPageSelected(position)
            action.invoke(position)
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            this@bindViewPager2.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            this@bindViewPager2.onPageScrollStateChanged(state)
        }
    })
}

fun MagicIndicator.bindReCommendViewPager2(
    viewPager: ViewPager2,
    mStringList: List<String> = arrayListOf(),
    action: (index: Int) -> Unit = {}) {
    val commonNavigator = CommonNavigator(App.instance)
    commonNavigator.adapter = RecommendIndicatorAdapter(mStringList,viewPager)
    this.navigator = commonNavigator

    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            this@bindReCommendViewPager2.onPageSelected(position)
            action.invoke(position)
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            this@bindReCommendViewPager2.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            this@bindReCommendViewPager2.onPageScrollStateChanged(state)
        }
    })
}

fun String.toHtml(flag: Int = Html.FROM_HTML_MODE_LEGACY): Spanned {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, flag)
    } else {
        Html.fromHtml(this)
    }
}

/**
 * 获取Navigation控制器
 */
fun nav(view: View): NavController {
    return Navigation.findNavController(view)
}

/**
 * 防止navigation连点发生崩溃
 */
private var lastClickTime = 0L
fun NavController.navigateNoRepeat(@IdRes resId: Int, args: Bundle?, interval: Long = 800) {
    val currentTime = System.currentTimeMillis()
    if (lastClickTime != 0L && (currentTime - lastClickTime < interval)) {
        return
    }
    lastClickTime = currentTime
    navigate(resId, args)
}

fun NavController.navigateNoRepeat(@IdRes resId: Int, interval: Long = 800) {
    val currentTime = System.currentTimeMillis()
    if (lastClickTime != 0L && (currentTime - lastClickTime < interval)) {
        return
    }
    lastClickTime = currentTime
    navigate(resId)
}

/**
 * navigate向上一层回传值
 * <p>
 *     在Navigation 2.3.0-alpha02及更高版本中，NavBackStackEntry允许访问SavedStateHandle。SavedStateHandle是一个键值映射，
 *     可用于存储和检索数据。这些值会在进程终止（包括配置更改）时保持不变，并通过同一对象保持可用。通过使用给定的SavedStateHandle，
 *     您可以在目标之间访问和传递数据。作为将数据从堆栈中弹出后从目的地取回数据的机制，这特别有用。
 *     若要将数据从目标B传递回目标A，请首先设置目标A在其SavedStateHandle上侦听结果。为此，请使用getCurrentBackStackEntry（）
 *     API检索NavBackStackEntry，然后观察SavedStateHandle提供的LiveData。
 * </p>
 */
fun NavController.navigateUpNoRepeat(args: Bundle? = null, interval: Long = 800) {
    val currentTime = System.currentTimeMillis()
    if (lastClickTime != 0L && (currentTime - lastClickTime < interval)) {
        return
    }
    lastClickTime = currentTime
    args?.let {
        previousBackStackEntry?.savedStateHandle?.set("args", args)
    }
    navigateUp()
}

/**
 * 此方法是注册一个observe来监听变化。所有建议注册在[BaseFragment.createObserver]中
 * <p>
 *     在目标B中，必须使用getPreviousBackStackEntry（）API在目标A的SavedStateHandle上设置结果。
 * </p>
 */
fun NavController.getResult(owner: LifecycleOwner, block: Bundle.() -> Unit) {
    currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>("args")
        ?.observe(owner, Observer {
            it.apply(block)
        })
}

/**
 * 整个框架的入口，核心
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
class MagicIndicator : FrameLayout {
    var navigator: IPagerNavigator? = null
        private set

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
    }

    fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
        if (navigator != null) {
            navigator!!.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }
    }

    fun onPageSelected(position: Int) {
        if (navigator != null) {
            navigator!!.onPageSelected(position)
        }
    }

    fun onPageScrollStateChanged(state: Int) {
        if (navigator != null) {
            navigator!!.onPageScrollStateChanged(state)
        }
    }

    fun setNavigator(navigator: IPagerNavigator) {
        if (this.navigator === navigator) {
            return
        }
        if (this.navigator != null) {
            navigator.onDetachFromMagicIndicator()
        }
        this.navigator = navigator
        removeAllViews()
        if (this.navigator is View) {
            val lp = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
            addView(this.navigator as View?, lp)
            navigator.onAttachToMagicIndicator()
        }
    }
}