package com.sprout.ui.custom

import android.content.Context
import android.media.MediaPlayer.OnPreparedListener
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.VideoView

/**
 * 视频播放 自定义 View
 */
class CustomVideoView : VideoView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //我们重新计算高度
        val width = View.getDefaultSize(0, widthMeasureSpec)
        val height = View.getDefaultSize(0, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun setOnPreparedListener(l: OnPreparedListener) {
        super.setOnPreparedListener(l)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return super.onKeyDown(keyCode, event)
    }
}
