package com.rick.newscasestudy.model

data class NewsFeed(
    val articles: List<Article>,
    val totalResults: Int
)