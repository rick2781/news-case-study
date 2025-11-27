package com.rick.newscasestudy.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rick.newscasestudy.data.paging.NewsPagingSource
import com.rick.newscasestudy.data.remote.NewsApi
import com.rick.newscasestudy.di.IoDispatcher
import com.rick.newscasestudy.model.Article
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NewsRepository {

    override fun getTopHeadlines(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, initialLoadSize = 20),
            pagingSourceFactory = { NewsPagingSource(api) }
        ).flow
    }

    override fun searchNews(searchQuery: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, initialLoadSize = 20),
            pagingSourceFactory = { NewsPagingSource(api, searchQuery) }
        ).flow
    }


}
