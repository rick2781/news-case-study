package com.rick.newscasestudy.data.mapper

import com.rick.newscasestudy.data.model.ArticleDto
import com.rick.newscasestudy.data.model.SourceDto
import org.junit.Assert.assertEquals
import org.junit.Test

class NewsMapperTest {

    @Test
    fun `toDomain maps ArticleDto to Article correctly`() {
        val articleDto = ArticleDto(
            source = SourceDto(id = "1", name = "Test Source"),
            author = "Test Author",
            title = "Test Title",
            description = "Test Description",
            url = "http://test.com",
            urlToImage = "http://test.com/image.jpg",
            publishedAt = "2023-01-01T12:00:00Z",
            content = "Test Content"
        )

        val article = articleDto.toDomain()

        assertEquals("Test Title", article.title)
        assertEquals("http://test.com", article.url)
        assertEquals("http://test.com/image.jpg", article.imageUrl)
        assertEquals("2023-01-01T12:00:00Z", article.publishedAt)
        assertEquals("Test Source", article.sourceName)
    }

    @Test
    fun `toDomain handles null values correctly`() {
        val articleDto = ArticleDto(
            source = null,
            author = null,
            title = null,
            description = null,
            url = null,
            urlToImage = null,
            publishedAt = null,
            content = null
        )

        val article = articleDto.toDomain()

        assertEquals("No Title", article.title)
        assertEquals("", article.url)
        assertEquals(null, article.imageUrl)
        assertEquals("", article.publishedAt)
        assertEquals("Unknown Source", article.sourceName)
    }
}
