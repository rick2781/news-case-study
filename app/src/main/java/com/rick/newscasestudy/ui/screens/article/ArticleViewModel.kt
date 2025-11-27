package com.rick.newscasestudy.ui.screens.article

import androidx.lifecycle.ViewModel
import com.rick.newscasestudy.analytics.AnalyticsHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {

    fun logArticleView(url: String) {
        analyticsHelper.logViewItem(itemId = url, contentType = "article")
    }
}