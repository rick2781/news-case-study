package com.rick.newscasestudy.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.rick.newscasestudy.model.Article
import javax.inject.Inject

class FirebaseAnalyticsHelper @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsHelper {

    override fun logSearch(searchTerm: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH) {
            param(FirebaseAnalytics.Param.SEARCH_TERM, searchTerm)
        }
    }

    override fun logViewItem(itemId: String, contentType: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_ID, itemId)
            param(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
        }
    }


}