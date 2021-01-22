package com.sprout.ui.main.addition

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.sprout.R
import kotlinx.android.synthetic.main.fragment_image.*

class ImageFragment (
    var index:Int,
    var path:String
): Fragment() {

    companion object{
        fun instance(i:Int,path:String):ImageFragment{
            return ImageFragment(i,path)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(path.isNotEmpty()){
            image.setImageURI(Uri.parse(path))
        }

    }

    /**
     * 添加标签到界面
     */
    fun addTagsToView(type:Int,id:Int,name:String){
        val view = layoutInflater.inflate(R.layout.layout_tag_item,null)
        val imgTag = view.findViewById<ImageView>(R.id.img_tag)
        val txtName = view.findViewById<TextView>(R.id.txt_name)
        txtName.text = name
        when(type){
            1-> imgTag.setImageResource(R.mipmap.tag_icon_w_brand)
            2-> imgTag.setImageResource(R.mipmap.tag_icon_w_commodity)
        }
        var param = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT)
        param.setMargins(100,100,0,0) //控制组件的坐标位置
        layout_tags.addView(view,param)
    }
}