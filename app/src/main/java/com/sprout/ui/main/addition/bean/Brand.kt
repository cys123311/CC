package com.sprout.ui.main.addition.bean

data class BrandData(
    val count: Int,
    val currentPage: Int,
    val `data`: List<Data>,
    val pageSize: Int,
    val totalPages: Int
) {
    data class Data(
        val id: Int,
        val name: String,
        val sort: Int,
        val url: String
    )
}

