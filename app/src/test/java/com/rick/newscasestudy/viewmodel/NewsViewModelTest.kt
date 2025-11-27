package com.rick.newscasestudy.viewmodel

import androidx.paging.PagingData
import com.rick.newscasestudy.analytics.AnalyticsHelper
import com.rick.newscasestudy.data.repository.NewsRepository
import com.rick.newscasestudy.model.Article
import com.rick.newscasestudy.ui.screens.news.NewsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    private val repository = mockk<NewsRepository>(relaxed = true)
    private val analyticsHelper = mockk<AnalyticsHelper>(relaxed = true)
    private lateinit var viewModel: NewsViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { repository.getTopHeadlines() } returns flowOf(PagingData.empty())
        every { repository.searchNews(any()) } returns flowOf(PagingData.empty())

        viewModel = NewsViewModel(repository, analyticsHelper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init calls getTopHeadlines`() = runTest {
        val job = launch {
            viewModel.articles.collect {}
        }
        testDispatcher.scheduler.advanceUntilIdle()
        verify { repository.getTopHeadlines() }
        job.cancel()
    }

    @Test
    fun `searchNews calls repository searchNews`() = runTest {
        val job = launch {
            viewModel.articles.collect {}
        }
        viewModel.searchNews("jiujitsu")
        testDispatcher.scheduler.advanceUntilIdle()
        verify { repository.searchNews("jiujitsu") }
        job.cancel()
    }

    @Test
    fun `searchNews logs analytics event`() = runTest {
        viewModel.searchNews("jiujitsu")
        verify { analyticsHelper.logSearch("jiujitsu") }
    }


}
