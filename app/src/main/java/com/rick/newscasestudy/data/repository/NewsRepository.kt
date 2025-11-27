package com.rick.newscasestudy.data.repository

import androidx.paging.PagingData
import com.rick.newscasestudy.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getTopHeadlines(): Flow<PagingData<Article>>
    fun searchNews(searchQuery: String): Flow<PagingData<Article>>
}
