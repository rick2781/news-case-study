package com.rick.newscasestudy.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    // Hardcoding for simplicity but there are better ways to store this such as in local.properties and not commiting to version control if it's sensitive
    private val apiKey = "f3e3594f508e4b1db25bf06a0c3803a5"

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url

        if (originalHttpUrl.host.contains("newsapi.org")) {
            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("apiKey", apiKey)
                .build()

            val requestBuilder = originalRequest.newBuilder()
                .url(url)

            val request = requestBuilder.build()
            return chain.proceed(request)
        }

        return chain.proceed(originalRequest)
    }
}
