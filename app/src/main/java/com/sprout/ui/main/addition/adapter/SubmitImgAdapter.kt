package com.sprout.ui.main.addition.adapter

import android.app.Activity
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.sprout.R
import com.sprout.base.BaseAdapter
import com.sprout.databinding.AdapterSubmitImgitemBinding
import com.sprout.ui.main.addition.SubmitMoreActivity
import com.sprout.ui.main.addition.bean.ImgData

class SubmitImgAdapter(context:Activity,val list :List<ImgData>) :
    BaseAdapter<AdapterSubmitImgitemBinding,ImgData>
        (context,list){
    lateinit var clickEvt: SubmitMoreActivity.SubmitClickEvt

    override fun convert(v: AdapterSubmitImgitemBinding, t: ImgData, position: Int) {
        val img = v.root.findViewById<ImageView>(R.id.img)
        val imgDelete = v.root.findViewById<ImageView>(R.id.img_delete)
        if(t.path.isNullOrEmpty()){
            img.setImageResource(R.drawable.ic_addimg)
            imgDelete.visibility = View.GONE
        }else{
            img.setImageURI(Uri.parse(t.path))
        }

        imgDelete.tag = t
        img.setOnClickListener {
            if(t.path.isNullOrEmpty()) return@setOnClickListener
            imgDelete.visibility = if (imgDelete.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        //点击 删除标记隐藏 再次点击 显示
        imgDelete.setOnClickListener {
            imgDelete.visibility = View.GONE
            clickEvt.clickDelete(imgDelete.tag as ImgData)
        }
    }
}
