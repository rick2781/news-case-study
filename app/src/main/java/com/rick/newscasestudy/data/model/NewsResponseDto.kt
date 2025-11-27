package com.rick.newscasestudy.data.model

import com.google.gson.annotations.SerializedName

data class NewsResponseDto(
    @SerializedName("status")
    val status: String?,
    @SerializedName("totalResults")
    val totalResults: Int?,
    @SerializedName("articles")
    val articles: List<ArticleDto>?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("message")
    val message: String?
)
