package com.rick.newscasestudy.data.remote

import com.rick.newscasestudy.data.model.NewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): NewsResponseDto

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): NewsResponseDto
}
