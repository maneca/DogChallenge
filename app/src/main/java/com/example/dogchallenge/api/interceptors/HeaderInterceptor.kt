package com.example.dogchallenge.api.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()

        val request: Request = original.newBuilder()
            .header("x-api-key", "e773e474-76b5-435a-8889-e3d2e94f1983")
            .method(original.method, original.body)
            .build()

        return chain.proceed(request)
    }
}