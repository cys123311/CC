package com.sprout.ui.main.login

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import com.sprout.MainActivity
import com.sprout.R
import com.sprout.ui.custom.CustomVideoView
import com.sprout.ui.custom.VerifyCodeView
import com.sprout.ui.main.HomeActivity
import com.sprout.utils.CountDownTimerUtils
import com.sprout.utils.ToastUtil
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(),View.OnClickListener {

    //创建播放视频的控件对象
    private var videoview: CustomVideoView? = null

    //验证码
    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //加载视频
        initView()
        //富文本
        Spannable()
        //点击
        initClick()
    }

    private fun initView() {
        //加载视频资源控件
        videoview = findViewById<CustomVideoView>(R.id.videoview_register)

        //设置播放加载路径
        videoview!!.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.login_bg));
//        videoview!!.setVideoPath(
//            Environment.getExternalStorageDirectory().toString() + "/Pictures/login_bg.mp4"
//        )
//        videoview!!.setVideoPath( "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4" )

        //播放
        videoview!!.start()
        //循环播放
        videoview!!.setOnCompletionListener { videoview!!.start() }

    }

    fun Spannable() {
        val string = tv_register_text.text.toString()
        //起始位置
        val startPos = string.indexOf(" ")
        //结束位置
        val endPos = string.indexOf("|")
        val spannableString = SpannableString(string)
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#FF9800")),
            startPos + 1,
            endPos,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        );//设置EZ的背景色
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#FF9800")),
            endPos + 1,
            spannableString.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        );//设置EZ前景色，也就是字体颜色
        tv_register_text.text = spannableString
    }

    //点击监听
    private fun initClick() {
        //手机登录注册
        btn_register_phone_login.setOnClickListener(this)
        //其他号码登录
        tv_rehister_else.setOnClickListener(this)
        //微博
        iv_register_wb.setOnClickListener(this)
        //微信
        iv_register_wx.setOnClickListener(this)
        //qq
        iv_register_qq.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            //手机登录注册
            R.id.btn_register_phone_login -> {
                intoneLogin()
            }
            //其他号码登录
            R.id.tv_rehister_else -> {
                initElse()
            }
            //微博
            R.id.iv_register_wb -> {
                ToastUtil.showToast(this, getString(R.string.wb))
            }
            //微信
            R.id.iv_register_wx -> {
                ToastUtil.showToast(this, getString(R.string.wc))
            }
            //qq
            R.id.iv_register_qq -> {
                ToastUtil.showToast(this, getString(R.string.qq))
            }
        }
    }

    //登录
    private fun intoneLogin() {
        val btnPhone = btn_register_phone_login.text.toString()
        if (btnPhone == "本机号码一键登录") {
            //跳转
            startActivity(Intent(this, HomeActivity::class.java))
        }
        if (btnPhone == "获取短信验证码") {
            val etPhone = et_register_phone.text.toString()
            if (!TextUtils.isEmpty(etPhone)) {
                if (etPhone.length == 11) {
                    //发送验证码
                    initCode()
                } else {
                    ToastUtil.showToast(this, getString(R.string.register_phone_code))
                }
            } else {
                ToastUtil.showToast(this, getString(R.string.register_phone))
            }
        }
    }

    //其他号码登录
    private fun initElse() {
        btn_register_phone_login.text = "获取短信验证码"
        //显示手机号
        ll_register_phone.visibility = View.VISIBLE
        //隐藏本机和其他
        tv_rehister_phone_local.visibility = View.GONE
        tv_rehister_else.visibility = View.GONE
    }

    //发送验证码
    private fun initCode() {
        tv_rehister_else.visibility = View.VISIBLE
        verify_code_view_register.visibility = View.VISIBLE
        tv_rehister_else.setTextColor(Color.WHITE)
        tv_rehister_else.text = "输入验证码"
        CountDownTimerUtils(btn_register_phone_login, 60000, 1000).start()
        //监听验证码
        initClickCode()
    }

    private fun initClickCode() {
        verify_code_view_register.setInputCompleteListener(object :
            VerifyCodeView.InputCompleteListener {
            override fun inputComplete() {
                var editContent = verify_code_view_register.editContent
                if (editContent!!.length == 6) {
                    //跳转
                    startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
                }
            }

            override fun invalidContent() {}
        })
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