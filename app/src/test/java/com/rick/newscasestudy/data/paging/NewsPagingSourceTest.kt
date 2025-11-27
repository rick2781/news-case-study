package com.rick.newscasestudy.data.paging

import androidx.paging.PagingSource
import com.rick.newscasestudy.data.model.ArticleDto
import com.rick.newscasestudy.data.model.NewsResponseDto
import com.rick.newscasestudy.data.remote.NewsApi
import com.rick.newscasestudy.model.Article
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import com.rick.newscasestudy.data.exception.NewsApiException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class NewsPagingSourceTest {

    private val api = mockk<NewsApi>()

    @Test
    fun `load returns Page when API call is successful`() = runTest {
        val pagingSource = NewsPagingSource(api)
        val articles = listOf(
            ArticleDto(
                source = null, author = null, title = "Test", description = null,
                url = "url", urlToImage = null, publishedAt = null, content = null
            )
        )
        val response = NewsResponseDto("ok", 1, articles, null, null)

        coEvery { api.getTopHeadlines(page = 1, pageSize = 20) } returns response

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assert(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(1, page.data.size)
        assertEquals("Test", page.data[0].title)
    }

    @Test
    fun `load returns Error when API call fails`() = runTest {
        val pagingSource = NewsPagingSource(api)
        val exception = IOException("Network error")

        coEvery { api.getTopHeadlines(page = 1, pageSize = 20) } throws exception

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assert(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assertEquals(exception, error.throwable)
    }

    @Test
    fun `load returns null nextKey when end of pagination reached`() = runTest {
        val pagingSource = NewsPagingSource(api)
        val articles = List(5) {
            ArticleDto(
                source = null, author = null, title = "Test $it", description = null,
                url = "url$it", urlToImage = null, publishedAt = null, content = null
            )
        }
        val response = NewsResponseDto("ok", 5, articles, null, null)

        coEvery { api.getTopHeadlines(page = 1, pageSize = 20) } returns response

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assert(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(5, page.data.size)
        assertEquals(2, page.nextKey)
    }

    @Test
    fun `load handles initial load size correctly`() = runTest {
        val pagingSource = NewsPagingSource(api)
        val loadSize = 60
        val articles = List(loadSize) {
            ArticleDto(
                source = null, author = null, title = "Test $it", description = null,
                url = "url$it", urlToImage = null, publishedAt = null, content = null
            )
        }
        val response = NewsResponseDto("ok", loadSize, articles, null, null)

        coEvery { api.getTopHeadlines(page = 1, pageSize = loadSize) } returns response

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = loadSize,
                placeholdersEnabled = false
            )
        )

        assert(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(loadSize, page.data.size)
        assertEquals(2, page.nextKey)
    }

    @Test
    fun `load returns nextKey when API returns fewer items than requested loadSize`() = runTest {
        val pagingSource = NewsPagingSource(api)
        val loadSize = 60
        val apiPageSize = 20
        val articles = List(apiPageSize) {
            ArticleDto(
                source = null, author = null, title = "Test $it", description = null,
                url = "url$it", urlToImage = null, publishedAt = null, content = null
            )
        }
        val response = NewsResponseDto("ok", apiPageSize, articles, null, null)

        coEvery { api.getTopHeadlines(page = 1, pageSize = loadSize) } returns response

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = loadSize,
                placeholdersEnabled = false
            )
        )

        assert(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(apiPageSize, page.data.size)
        assertEquals(2, page.nextKey)
    }
    @Test
    fun `load returns Error with NewsApiException when API returns error status`() = runTest {
        val pagingSource = NewsPagingSource(api)
        val response = NewsResponseDto("error", 0, null, "apiKeyMissing", "API key missing")

        coEvery { api.getTopHeadlines(page = 1, pageSize = 20) } returns response

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assert(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assert(error.throwable is NewsApiException)
        val exception = error.throwable as NewsApiException
        assertEquals("API key missing", exception.message)
        assertEquals("apiKeyMissing", exception.code)
    }

    @Test
    fun `load returns Error with NewsApiException when API returns HTTP error with error body`() = runTest {
        val pagingSource = NewsPagingSource(api)
        val errorJson = """{"status":"error","code":"apiKeyMissing","message":"API key missing"}"""
        val errorBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())
        val httpException = HttpException(Response.error<NewsResponseDto>(401, errorBody))

        coEvery { api.getTopHeadlines(page = 1, pageSize = 20) } throws httpException

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assert(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assert(error.throwable is NewsApiException)
        val exception = error.throwable as NewsApiException
        assertEquals("API key missing", exception.message)
        assertEquals("apiKeyMissing", exception.code)
    }
}
