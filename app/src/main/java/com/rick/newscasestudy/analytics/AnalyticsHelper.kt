package com.rick.newscasestudy.analytics

import com.rick.newscasestudy.model.Article

interface AnalyticsHelper {
    fun logSearch(searchTerm: String)
    fun logViewItem(itemId: String, contentType: String)
}