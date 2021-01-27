package com.sprout.ui.main.addition

import android.content.Intent
import android.location.Address
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.sprout.base.BaseActivity
import com.sprout.databinding.ActivitySubmitMoreBinding
import com.sprout.ui.main.addition.adapter.SubmitImgAdapter
import com.sprout.ui.main.addition.bean.ImgData
import com.sprout.ui.main.addition.bean.LZSubmitTrends
import com.sprout.utils.GlideEngine
import com.sprout.utils.LocationUtils
import com.sprout.widget.clicks
import kotlinx.android.synthetic.main.activity_submit_more.*
import org.json.JSONArray

/**
 * 动态数据的提交
 */
class SubmitMoreActivity :
    BaseActivity<SubmitViewModel,ActivitySubmitMoreBinding>() {

    var imgs : MutableList<ImgData> = mutableListOf()
    var max_img = 9

    lateinit var imgAdapter: SubmitImgAdapter

    override fun initView() {
        imgAdapter = SubmitImgAdapter(this,imgs)
        recyImgs.layoutManager = GridLayoutManager(this,3)
        recyImgs.adapter = imgAdapter
        imgAdapter.clickEvt = SubmitClickEvt()
        /**
         * item条目的点击
         */
        imgAdapter.itemClick {
            //当前点击的是加号
            if(it  == imgs.size+1 ){
                openPhoto()
            }
            Log.e("imgAdapter","1")
        }

        //经纬度 地点获取
        val address:Address?=null
        LocationUtils.getInstance(this).addressCallback.also {
            val  add = it.onGetAddress(address)
            Log.e("111",add.toString())
        }
    }
//
//    //注意6.0及以上版本需要在申请完权限后调用方法
//    LocationUtils.getInstance(this).setAddressCallback(new LocationUtils.AddressCallback() {
//    @Override
//    public void onGetAddress(Address address) {
//    String countryName = address.getCountryName();//国家
//    String adminArea = address.getAdminArea();//省
//    String locality = address.getLocality();//市
//    String subLocality = address.getSubLocality();//区
//    String featureName = address.getFeatureName();//街道
//    LogUtils.eTag("定位地址",countryName,adminArea,locality,subLocality,featureName);
//    }
//
//    @Override
//    public void onGetLocation(double lat, double lng) {
//    LogUtils.eTag("定位经纬度",lat,lng);
//    }
//    });

    override fun initData() {
        var json = intent.getStringExtra("data")
        if(json!!.isNotEmpty()){
            var jsonArr = JSONArray(json)
            for(i in 0 until jsonArr.length()){
                var imgData = Gson().fromJson<ImgData>(jsonArr.getString(i),ImgData::class.java)
                imgs.add(imgData)
            }
            //处理加号
            if(imgs.size < max_img){
                var imgData = ImgData(null, mutableListOf())
                imgs.add(imgData)
            }
        }
    }


    private fun openPhoto(){
        //当前还能插入的图片数量
        val num = max_img-imgs.size+1
        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())
            .loadImageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
            .maxSelectNum(num)
            .imageSpanCount(3)
            .selectionMode(PictureConfig.MULTIPLE)
            .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    /**
     * 打开activity后回传
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PictureConfig.CHOOSE_REQUEST -> {
                // onResult Callback
                val selectList = PictureSelector.obtainMultipleResult(data)
                if (selectList.size == 0) return
                //获取本地图片的选择地址，上传到服务器
                //头像的压缩和二次采样
                //把选中的图片插入到列表
                for(i in 0 until selectList.size){
                    var imgData = ImgData(selectList.get(i).path, mutableListOf())
                    var index = imgs.size-1
                    imgs.add(index,imgData)
                }
                if(imgs.size > max_img){
//                    imgs.removeLast()
                }
                imgAdapter.notifyDataSetChanged()

            }

            else -> {
            }
        }
    }

    inner class SubmitClickEvt{
        fun clickDelete(data: ImgData){
            imgs.remove(data)
            imgAdapter.notifyDataSetChanged()
        }
    }

    override fun initClick() {
        //地址

        //发布
//        v.txtSubmit.clicks {
//            val submit = LZSubmitTrends(
//                "北京",
//                1,
//                127,
//                45,
//                "开心",
//                res,
//                1,
//                v.editTitle.text,
//                1
//            )
//            vm.submitTrends(,true)
//        }
    }

    override fun initVM() {

    }
}
