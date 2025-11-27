package com.rick.newscasestudy.data.exception

class NewsApiException(message: String, val code: String? = null) : Exception(message)
