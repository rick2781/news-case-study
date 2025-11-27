package com.rick.newscasestudy.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rick.newscasestudy.data.mapper.toDomain
import com.rick.newscasestudy.data.remote.NewsApi
import com.rick.newscasestudy.model.Article
import com.google.gson.Gson
import com.rick.newscasestudy.data.exception.NewsApiException
import com.rick.newscasestudy.data.model.NewsResponseDto
import retrofit2.HttpException
import java.io.IOException

class NewsPagingSource(
    private val newsApi: NewsApi,
    private val query: String? = null
) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        return try {
            val response = if (query.isNullOrBlank()) {
                newsApi.getTopHeadlines(page = page, pageSize = params.loadSize)
            } else {
                newsApi.searchNews(searchQuery = query, page = page, pageSize = params.loadSize)
            }

            if (response.status == "error") {
                return LoadResult.Error(NewsApiException(response.message ?: "Unknown error", response.code))
            }

            val articles = response.articles?.map { it.toDomain() } ?: emptyList()
            
            val nextKey = if (articles.isEmpty()) {
                null
            } else {
                page + 1
            }

            LoadResult.Page(
                data = articles,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                try {
                    val errorResponse = Gson().fromJson(errorBody, NewsResponseDto::class.java)
                    if (errorResponse.status == "error") {
                        return LoadResult.Error(NewsApiException(errorResponse.message ?: "Unknown error", errorResponse.code))
                    }
                } catch (parseException: Exception) {
                    // Fallback to original exception
                }
            }
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
