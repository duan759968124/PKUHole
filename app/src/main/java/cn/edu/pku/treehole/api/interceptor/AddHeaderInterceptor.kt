package cn.edu.pku.treehole.api.interceptor

import cn.edu.pku.treehole.utilities.AppUtil
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AddHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest : Request = chain.request()
            .newBuilder()
            .addHeader("X-PKUHelper-API", "3.0")
            .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
//            .addHeader("User-Agent", "Android3.0.8_Xiaomi9_455089410")
            .addHeader("User-Agent", AppUtil.getUserAgent())
            .build()
        return chain.proceed(newRequest)
    }
}