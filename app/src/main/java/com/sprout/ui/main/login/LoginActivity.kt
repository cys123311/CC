package com.sprout.ui.main.login

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.sprout.R
import com.sprout.ui.custom.CustomVideoView

/**
 * 视频资源要添加res文件夹下创建raw文件夹
 * 需要在onRestart()方法里重新加载视频，防止退出返回时视频黑屏
 * 我这样写简单粗暴而已，当然，也可优雅的以自己看播放控件的VideoView处理方法，去处理资源释放和播放显示的问题。
 * 记得修改布局控件<com.daqie.videobackground.CustomVideoView...引用的包名></com.daqie.videobackground.CustomVideoView...引用的包名>，不然会报错哦
 * android:screenOrientation="portrait" 习惯性的把横竖屏切换也设置一下
 * android:theme="@style/Theme.AppCompat.Light.NoActionBar" ActionBar也可以设置成不显示的状态，可以根据自己喜好和项目需求
 */
class LoginActivity : AppCompatActivity() {


    //创建播放视频的控件对象
    private var videoview: CustomVideoView? = null
    private var mText2Tv: TextView? = null
    private var mText1Tv: TextView? = null
    private var clickableSpan: ClickableSpan? = null
    private var clickableSpan2: ClickableSpan? = null
    private val clickableSpan3: ClickableSpan? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //加载数据
        initView()
    }

    private fun initView() {
        //加载视频资源控件
        videoview = findViewById<CustomVideoView>(R.id.videoview)

        //设置播放加载路径
        videoview!!.setVideoPath(
            Environment.getExternalStorageDirectory().toString() + "/Pictures/boy.mp4"
        )
        //videoview!!.setVideoPath( "
        // " );

        //播放
        videoview!!.start()
        //循环播放
        videoview!!.setOnCompletionListener { videoview!!.start() }
        mText2Tv = findViewById(R.id.tv_text2) as TextView
        mText1Tv = findViewById(R.id.tv_text1) as TextView

        //富文本
        Spannable()
    }

    private fun Spannable() {

        val string = mText2Tv!!.text.toString()
        val start = string.indexOf(" ")
        val end = string.indexOf("|")
        val span = SpannableStringBuilder(string)

        //设置EZ的背景色

        span.setSpan(
            ForegroundColorSpan(Color.parseColor("#FF9800")),
            start + 1,
            end,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        //监听
        clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                (view as? TextView)?.highlightColor = Color.TRANSPARENT//去除背景色
                Toast.makeText(this@LoginActivity, "用户协议", Toast.LENGTH_SHORT).show()
            }

            override fun updateDrawState(ds: TextPaint) { //设置显示样式
                ds.isUnderlineText = false //不要默认下划线
                ds.isUnderlineText = false//去除下划线

            }
        }

        span.setSpan(clickableSpan, start + 1, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        span.setSpan(
            ForegroundColorSpan(Color.parseColor("#FF9800")),
            end + 1,
            span.length,
            android.text.Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )

        //设置EZ前景色，也就是字体颜色
        mText2Tv!!.movementMethod = LinkMovementMethod.getInstance()
        mText2Tv!!.text = span

        val string2 = mText1Tv!!.text.toString()
        val start2 = string2.indexOf(" ")
        val span2 = SpannableStringBuilder(string2)
        span2.setSpan(
            ForegroundColorSpan(Color.parseColor("#0099EE")),
            start2 + 1,
            span2.length,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )
        //设置EZ的背景色
        clickableSpan2 = object : ClickableSpan() {
            override fun onClick(view: View) {
                (view as? TextView)?.highlightColor = Color.TRANSPARENT//去除背景色
                Toast.makeText(this@LoginActivity, " 中国移动认证", Toast.LENGTH_SHORT).show()
            }
            override fun updateDrawState(ds: TextPaint) { //设置显示样式
                ds.isUnderlineText = false //不要默认下划线
                ds.isUnderlineText = false//去除下划线

            }
        }
        span2.setSpan(clickableSpan2, start2 + 1, span2.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        mText1Tv!!.movementMethod = LinkMovementMethod.getInstance()
        mText1Tv!!.text = span2
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