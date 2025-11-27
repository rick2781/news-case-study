package com.rick.newscasestudy.ui.screens.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rick.newscasestudy.analytics.AnalyticsHelper
import com.rick.newscasestudy.data.repository.NewsRepository
import com.rick.newscasestudy.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val articles: Flow<PagingData<Article>> = _searchQuery.flatMapLatest { query ->
        if (query.isEmpty()) {
            repository.getTopHeadlines()
        } else {
            repository.searchNews(query)
        }
    }.cachedIn(viewModelScope)

    fun searchNews(query: String) {
        _searchQuery.value = query
        logSearchEvent(query)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getTopHeadlines() {
        _searchQuery.value = ""
    }

    private fun logSearchEvent(query: String) {
        analyticsHelper.logSearch(query)
    }
}