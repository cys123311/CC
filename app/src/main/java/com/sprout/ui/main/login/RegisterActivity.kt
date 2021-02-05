package com.sprout.ui.main.login

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.Observer
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.sprout.R
import com.sprout.api.URLConstant
import com.sprout.base.BaseActivity
import com.sprout.databinding.ActivityRegisterBinding
import com.sprout.ui.custom.CustomVideoView
import com.sprout.ui.main.HomeActivity
import com.sprout.utils.LocationUtils
import com.sprout.utils.MyMmkv
import com.sprout.utils.ToastUtil
import kotlin.math.log

class RegisterActivity :
    BaseActivity<RegisterViewModel, ActivityRegisterBinding>(),
    AMapLocationListener {

    //创建播放视频的控件对象
    private var videoview: CustomVideoView? = null

    lateinit var mLocationClient: AMapLocationClient
    lateinit var mLocationOption: AMapLocationClientOption
    private var isRegister = false

    override fun initView() {
        //加载视频资源控件
        videoview = findViewById<CustomVideoView>(R.id.videoview_register)

        //设置播放加载路径
        videoview!!.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.smile))
        //播放
        videoview!!.start()
        //循环播放
        videoview!!.setOnCompletionListener { videoview!!.start() }

        v.loginClick = ProxyClick()

    }

    override fun initClick() {

    }

    override fun initData() {

    }

    lateinit var username: String
    lateinit var userPsw: String

    override fun initVM() {
        //加载数据
        vm.run {

            registerInfo.observe(mContext, Observer {
                if (isRegister) {
                    if (it.userInfo == null) {
                        ToastUtil.showToast(mContext, "用户名已注册")
                    } else {
                        ToastUtil.showToast(mContext, "注册成功！请登录")
                        ProxyClick().register()
                    }
                } else {
                    if (it.code == 200) {
                        it.token?.let { it1 ->
                            MyMmkv.setValue(URLConstant.token, it1)
                            MyMmkv.setValue("long", true)
                            Log.e("token",MyMmkv.getString(URLConstant.token)+"")
                        }

                        ToastUtil.showToast(mContext, "登录成功！")
                        startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
                    } else {
                        ToastUtil.showToast(mContext, "登录失败！请检查账号密码是否正确！")
                    }
                }
            })
        }
    }

    inner class ProxyClick {

        //注册和登录页面的切换
        fun register() {

            if (isRegister) {

                v.txtRegisBtn.text = "注册"
                v.txtLoginCommitBtn.text = "登录"
            } else {
                v.txtRegisBtn.text = "登录"
                v.txtLoginCommitBtn.text = "注册"
            }

            isRegister = !isRegister
        }

        // 注册和登录接口的提交
        fun loginBtnCommit() {

            username = v.editLoginPhoneNumber.text.toString()
            userPsw = v.editLoginPsw.text.toString()
            when {
                username.isEmpty() -> ToastUtil.showToast(mContext, "请填写账号")
                userPsw.isEmpty() -> ToastUtil.showToast(mContext, "请填写密码")
                else -> if (isRegister) {
                    location()//注册
                } else {
                    vm.login(username, userPsw)//登录
                }
            }
        }
    }

    /*开启定位*/
    private fun location() {
        //初始化定位
        mLocationClient =
            AMapLocationClient(mContext)
        //设置定位回调监听
        mLocationClient.setLocationListener(this)
        //初始化定位参数
        mLocationOption = AMapLocationClientOption()
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true)
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
        //启动定位
        mLocationClient.startLocation()
    }

    @SuppressLint("HardwareIds")
    override fun onLocationChanged(p0: AMapLocation) {
        val lat = p0.latitude //获取纬度
        val lon = p0.longitude //获取经度

        /**
         * AndroidId
         */
        val m_szAndroidID: String = Settings.Secure.getString(
            mContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )

        vm.register(
            username,
            userPsw,
            imei = m_szAndroidID,
            lat = lat.toString(),
            lng = lon.toString()
        )
        Log.e("111", "ok")
    }


    //返回重启加载
    override fun onRestart() {
        initView()
        super.onRestart()
    }

    //防止锁屏或者切出的时候，音乐在播放
    override fun onStop() {
        videoview!!.stopPlayback()
        super.onStop()
    }
}