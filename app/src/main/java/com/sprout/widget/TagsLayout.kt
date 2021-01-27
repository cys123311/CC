package com.sprout.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.sprout.ui.main.addition.bean.ImgData

/**
 * 具有拖动功能的容器
 */
class TagsLayout:FrameLayout,View.OnTouchListener {

    constructor(context: Context) : super(context) {
        this.setOnTouchListener(this)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.setOnTouchListener(this)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.setOnTouchListener(this)
    }

    var startX=0
    var startY=0
    var curView:View? = null    //当前选中的容器
    //var tags:MutableList<ImgData.Tag>? = null  //当前标签的tag对象

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_DOWN -> {
                if(v is ConstraintLayout){
                    curView = v
                    startX = event.rawX.toInt()
                    startY = event.rawY.toInt()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if(curView != null){
                    var x:Int = event.rawX.toInt()
                    var y:Int = event.rawY.toInt()
                    var dx = x - startX
                    var dy = y - startY
                    var l = v!!.left
                    var r = v.right
                    var t = v.top
                    var b = v.bottom
                    curView!!.layout(l+dx,t+dy,r+dx,b+dy)
                    if(v!!.tag != null){
                        val tag = v.tag as ImgData.Tag
                        tag!!.x = (l+dx).toFloat()
                        tag!!.y = (t+dy).toFloat()
                    }
                }
                startX = event.getRawX().toInt()
                startY = event.getRawY().toInt()
            }
            MotionEvent.ACTION_UP -> {
                curView = null
            }
        }
        return true
    }
}