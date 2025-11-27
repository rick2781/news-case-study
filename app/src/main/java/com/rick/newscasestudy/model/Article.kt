package com.rick.newscasestudy.model

data class Article(
    val title: String,
    val url: String,
    val imageUrl: String?,
    val publishedAt: String,
    val sourceName: String
)