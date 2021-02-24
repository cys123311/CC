package com.sprout.utils

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.ImageViewerPopupView
import com.lxj.xpopup.interfaces.XPopupImageLoader
import java.io.File

/**
 * XPopup 弹窗库
 */
object XPopupUtils {

    fun setImg0(context: Context, imageView: ImageView, url: String) {
        XPopup.Builder(context)
            .isDestroyOnDismiss(true)
            .asImageViewer(
                imageView, url, true,
                Color.parseColor("#f1f1f1"),
                -1,
                0,
                false,
                ImageLoader()
            )
            .show()
    }

    fun setImg1(context: Context, imageView: ImageView, url: String) {
        XPopup.Builder(context)
            .asImageViewer(imageView, url, ImageLoader())
            .show()
    }

    fun setImg4(context: Context) {
        XPopup.Builder(context)
            .asBottomList(
                "", arrayOf("再次编辑", "删除图片", "取消")
            ) { position2, text ->
                when (position2) {
                    0 -> {

                    }
                }
            }.show()
    }

//    fun setImg2(context:Context,imageView:ImageView,url:String){
//        XPopup.Builder(context)
//            .asImageViewer(imageView, position, list, { popupView, position ->
//                val rv = holder.itemView.getParent() as RecyclerView
//                popupView.updateSrcView(rv.getChildAt(position) as ImageView)
//            }, ImageLoader()
//        ).show()
//    }

    fun setImg3(context: Context, imageView: ImageView, url: String, layoutId: Int) {

        //自定义的弹窗需要用asCustom来显示，之前的asImageViewer这些方法当然不能用了。
        val viewerPopup = CustomImageViewerPopup(context, layoutId)
        //自定义的ImageViewer弹窗需要自己手动设置相应的属性，必须设置的有srcView，url和imageLoader。
        //自定义的ImageViewer弹窗需要自己手动设置相应的属性，必须设置的有srcView，url和imageLoader。
        viewerPopup.setSingleSrcView(imageView, url)
//                viewerPopup.isInfinite(true);
        //                viewerPopup.isInfinite(true);
        viewerPopup.setXPopupImageLoader(ImageLoader())
//                viewerPopup.isShowIndicator(false);//是否显示页码指示器
//                viewerPopup.isShowPlaceholder(false);//是否显示白色占位块
//                viewerPopup.isShowSaveButton(false);//是否显示保存按钮
        //                viewerPopup.isShowIndicator(false);//是否显示页码指示器
//                viewerPopup.isShowPlaceholder(false);//是否显示白色占位块
//                viewerPopup.isShowSaveButton(false);//是否显示保存按钮
        XPopup.Builder(context)
            .isDestroyOnDismiss(true)
            .asCustom(viewerPopup)
            .show()
    }

    fun setImg5(context: Context){
//        XPopup.Builder(context)
//            //.maxWidth(600)
//            //.maxWidth((ScreenUtils.getScreenWidth(this) * 0.7).toInt()) //弹窗所在屏幕宽度占比
//            .isDarkTheme(true)
//            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
//            .asCenterList("请选择一项", arrayOf("条目1", "条目2", "条目3", "条目4"),
//                object : OnSelectListener() {
//                    fun onSelect(position: Int, text: String) {
//                        toast("click $text")
//                    }
//                }) //                        .bindLayout(R.layout.my_custom_attach_popup) //自定义布局
//            .show()
    }

    class ImageLoader : XPopupImageLoader {
        override fun loadImage(
            position: Int,
            url: Any,
            imageView: ImageView
        ) {
            //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
            Glide.with(imageView).load(url)
                .apply(RequestOptions().override(Target.SIZE_ORIGINAL))
                .into(imageView)
        }

        override fun getImageFile(
            context: Context,
            uri: Any
        ): File? {
            try {
                return Glide.with(context).downloadOnly().load(uri).submit().get()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }

    /**
     * Description: 自定义大图浏览弹窗
     * Create by dance, at 2019/5/8
     */
    class CustomImageViewerPopup(context: Context, private val layoutId: Int) :
        ImageViewerPopupView(context) {
        override fun getImplLayoutId(): Int {
            return layoutId
        }
    }
}