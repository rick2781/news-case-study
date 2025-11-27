package com.rick.newscasestudy.data.mapper

import com.rick.newscasestudy.data.model.ArticleDto
import com.rick.newscasestudy.data.model.NewsResponseDto
import com.rick.newscasestudy.model.Article
import com.rick.newscasestudy.model.NewsFeed

fun ArticleDto.toDomain(): Article {
    return Article(
        title = this.title ?: "No Title",
        url = this.url ?: "",
        imageUrl = this.urlToImage,
        publishedAt = this.publishedAt ?: "",
        sourceName = this.source?.name ?: "Unknown Source"
    )
}
