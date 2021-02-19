package com.sprout.utils

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.PopupWindow
import androidx.navigation.NavController
import com.lxj.xpopup.XPopup
import com.sprout.api.ext.nav
import com.sprout.api.ext.navigateUpNoRepeat

/**
 * 弹窗进本设置
 */
class PwUtils {

    /**
     * 伴生对象 static
     */
    companion object {

        //统一设置 pop布局 宽高
        fun getPwXY(): PopupWindow {
            return PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, 600)
        }

        //开启阴影
        fun openShadow(window: Window) {
            val attributes = window.attributes
            attributes.alpha = 0.8f
            window.attributes = attributes
        }

        //关闭阴影
        fun closeShadow(window: Window) {
            //关闭阴影
            val attributes = window.attributes
            attributes.alpha = 1f
            window.attributes = attributes
        }

        //另一种 pop 学习中
        fun c(context: Context){
//            XPopup.Builder(context)
//                .asBottomList(
//                    "", arrayOf("再次编辑", "删除图片", "取消")
//                ) { position2, text ->
//                    when (position2) {
//                        0 -> {
//                            nav().navigateUpNoRepeat(Bundle().apply {
//                                putStringArrayList("imgList", list)
//                            })
//                        }
//                    }
//                }.show()
        }

    }
}