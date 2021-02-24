package com.sprout.ui.main.addition.bean


/**
 * 图标编辑页面数据
 */
data class ImgData(
    var path: String?,
    var tags: MutableList<Tag>,
    var type: Int = 1 //type 1 图片 2视频
) {
    data class Tag(
        var id: Int,
        var x: Float,
        var y: Float,
        var type: Int,
        var name: String,
        var lng: Double,
        var lat: Double
    )
}
