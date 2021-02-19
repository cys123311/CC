package com.sprout.ui.main.music

import com.lzx.musiclibrary.aidl.model.SongInfo
import com.lzx.musiclibrary.cache.CacheConfig
import com.lzx.musiclibrary.cache.CacheUtils
import com.lzx.musiclibrary.manager.MusicLibrary
import com.lzx.musiclibrary.manager.MusicManager
import com.lzx.musiclibrary.notification.NotificationCreater
import com.lzx.musiclibrary.notification.PendingIntentMode
import com.lzx.musiclibrary.utils.BaseUtil
import com.sprout.base.BaseActivity
import com.sprout.databinding.ActivityMusicBinding


class MusicActivity :
    BaseActivity<MusicViewModel,ActivityMusicBinding>() {

    override fun initView() {

        if (BaseUtil.getCurProcessName(this).equals("com.sprout")) {
            val musicLibrary: MusicLibrary = MusicLibrary.Builder(this)
                .build()
            musicLibrary.startMusicService()
        }

        //Notification configuration

        //Notification configuration
        val creater = NotificationCreater.Builder()
            .setTargetClass("com.lzx.nicemusic.module.main.HomeActivity")
            .setCreateSystemNotification(true)
            .setNotificationCanClearBySystemBtn(true)
            .setSystemNotificationShowTime(true)
            .setPendingIntentMode(PendingIntentMode.MODE_ACTIVITY)
            .build()

        //边播边存配置
        val cacheConfig = CacheConfig.Builder()
            .setOpenCacheWhenPlaying(true)
            .setCachePath(CacheUtils.getStorageDirectoryPath() + "/NiceMusic/Cache/")
            .build()

        val musicLibrary = MusicLibrary.Builder(this)
            .setNotificationCreater(creater)
            .setCacheConfig(cacheConfig)
            .setUseMediaPlayer(false)
            .build()
        musicLibrary.startMusicService()
    }

    override fun initClick() {

    }

    override fun initData() {
        //简单播放一首歌曲
        val info = SongInfo() //songId 是该音频的唯一标识。
        info.songId = "111"
        info.songUrl = "http://music.163.com/song/media/outer/url?id=317151.mp3"
        MusicManager.get().playMusicByInfo(info)
    }

    override fun initVM() {

    }

}