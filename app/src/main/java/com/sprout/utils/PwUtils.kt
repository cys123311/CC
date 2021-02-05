package com.sprout.utils

import android.view.ViewGroup
import android.view.Window
import android.widget.PopupWindow

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
    }
}