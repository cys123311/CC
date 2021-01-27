package com.sprout.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.sprout.R
import com.sprout.base.BaseActivity
import com.sprout.base.BaseViewModel
import com.sprout.databinding.ActivitySplashBinding
import com.sprout.ui.main.HomeActivity
import com.sprout.ui.main.InitActivity
import com.sprout.ui.main.login.RegisterActivity
import com.sprout.utils.MyMmkv
import com.sprout.utils.StatusBarUtil
import com.sprout.utils.SysUtils
import com.sprout.widget.clicks
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.layout_guidance_pop.view.*

class SplashActivity : BaseActivity<BaseViewModel, ActivitySplashBinding>() {

    private var popupWindow: PopupWindow? = null
    private var guidance : Boolean = MyMmkv.getBool("guidance") //是否进入过引导页
    private var long : Boolean = MyMmkv.getBool("long")//是否登录过

    override fun initView() {
        StatusBarUtil.immersive(this)
        StatusBarUtil.darkMode(this)
        if (!this.isTaskRoot) {
            val mainIntent = intent
            val action = mainIntent.action
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action == Intent.ACTION_MAIN) {
                finish()
                return
            }
        }

        //判断 sdk 版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasPermission()) {
            //申请权限
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 1001
            )
        } else {
            //不用申请权限  判断是否登录过 跳转到登录界面
            if (long) {
                //登录过 进入首页
                initHome()
            } else {
                //没有登陆过 进入登录
                initRegister()
            }
        }
    }

    //首页
    private fun initHome() {
        SysUtils.initFiles()
        Handler().postDelayed({
            startActivity(Intent(mContext, HomeActivity::class.java))
            finish()
        }, 2000)
    }

    //登录
    private fun initRegister() {
        SysUtils.initFiles()
        Handler().postDelayed({
            startActivity(Intent(mContext, RegisterActivity::class.java))
            finish()
        }, 2000)
    }

    override fun initClick() {

    }

    override fun initData() {

    }

    override fun initVM() {

    }

    private fun initPw() {
        val popupView: View = LayoutInflater.from(this)
            .inflate(R.layout.layout_guidance_pop, null)
        //设置popu
        popupWindow = PopupWindow(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //开启阴影
        val attributes = window.attributes
        attributes.alpha = 0.5f
        window.attributes = attributes
        //找到视图
        popupWindow!!.contentView = popupView
        popupWindow!!.isClippingEnabled = false

        initText(popupView)

        //点击不同意 返回上一页面 退出应用
        popupView.btn_guidance_no.clicks {
            popupWindow!!.dismiss()//关闭弹窗
            initPwNo()
            finishAndRemoveTask()
        }

        //点击同意 进入下一界面 登录
        popupView.btn_guidance_ok.clicks {
            popupWindow!!.dismiss()//关闭弹窗
            initPwNo()
            initRegister()
            val ok = "已经进入过引导页"
            MyMmkv.setValue("guidance",true)
//            SpUtils.instance!!.setValue("guidance", ok)
        }

        //在按钮的下方弹出  无偏移 第一种方式
        popupWindow!!.showAtLocation(bk_guidance, Gravity.CENTER, 0, 0) //开启弹窗

    }

    private fun initText(popupView: View) {
        //富文本
        val spannableString =
            SpannableString("3、你可阅读《用户协议》《隐私条款》了解详细信息，如你[同意]，可点击同意开始接受我们的服务。");
        //起始位置
        var startPos = spannableString.indexOf("《")
        //结束位置
        var endPos = spannableString.lastIndexOf("》") + 1
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#80A55F")),
            startPos, endPos, Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )
        //下划线
        val underlineSpan = UnderlineSpan()
        spannableString.setSpan(underlineSpan, 6, 18, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        //赋值
        popupView.tv_guidance_one.text =
            "1、为了你的使用体验,缓存图片和视频,降低流量消耗,我们会申请存储权限,同时,为了账号安全,防止被盗,我们会申请系统权限手机设备信息,其他敏感权限如摄像头、麦克风、位置，仅会在使用相关功能时经过明示授权才会开启。"
        popupView.tv_guidance_two.text =
            "2、绿洲采取严格的数据安全措施保护您的个人信息安全，未经您的同意，我们不会自第三方获取、共享或对外提供你的个人信息，您也可以随时更正或删除您的个人信息。"
        popupView.tv_guidance_three.text = spannableString
    }

    //关闭阴影
    private fun initPwNo() {
        //关闭阴影
        val attributes = window.attributes
        attributes.alpha = 1f
        window.attributes = attributes
    }

    override fun onDestroy() {
        super.onDestroy()
        if (popupWindow != null && popupWindow!!.isShowing) {
            popupWindow!!.dismiss()
            popupWindow = null
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1001 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //判断 是否登陆过 为true 进入首页
                    if (long) {
                        //是否为首次登录 进入爱好选择
//                        if(guidance){
//                            startActivity(Intent(mContext, InitActivity::class.java))
//                            finish()
//                        }
                        initHome()
                    } else {
                        //pop 再次确认 进入登录
                        initPw()
                    }

                } else {
                    Toast.makeText(mContext, "您拒绝了文件权限", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    private fun hasPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission_group.STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        else
            true
    }
}