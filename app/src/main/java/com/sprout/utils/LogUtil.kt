package com.sprout.utils

import android.util.Log
import com.sprout.BuildConfig

object LogUtil {
    private const val TAG = "mvvm_log"
    private const val TAG_NET = "mvvm_net"

    fun i(message: String?) {
        if (BuildConfig.DEBUG) message?.let { Log.i(TAG, it) }
    }

    fun e(message: String?) {
        if (BuildConfig.DEBUG) message?.let { Log.e(TAG, it) }
    }

    fun showHttpHeaderLog(message: String?) {
        if (BuildConfig.DEBUG) message?.let { Log.d(TAG_NET, it) }
    }

    fun showHttpApiLog(message: String?) {
        if (BuildConfig.DEBUG) message?.let { Log.w(TAG_NET, it) }
    }

    fun showHttpLog(message: String?) {
        if (BuildConfig.DEBUG) message?.let { Log.i(TAG_NET, it) }
    }
}