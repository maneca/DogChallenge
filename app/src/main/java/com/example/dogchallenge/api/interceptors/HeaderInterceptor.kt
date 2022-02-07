package com.example.dogchallenge.api.interceptors

import android.content.Context
import com.example.dogchallenge.R
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor(context: Context?) : Interceptor {

    private var mContext: Context? = context

    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()

        val request: Request = original.newBuilder()
            .header("x-api-key", mContext?.resources?.getString(R.string.api_key) ?: "")
            .method(original.method, original.body)
            .build()

        return chain.proceed(request)
    }
}