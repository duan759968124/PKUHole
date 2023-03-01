package cn.edu.pku.treehole.api.interceptor

import cn.edu.pku.treehole.utilities.TEST_HOST_ADDRESS
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class ChangeBaseUrlInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        val oldHttpUrl = oldRequest.url
//        val isIsop = oldHttpUrl.toString().contains("svcpub/svc")
//        if (!isIsop) {
//            return chain.proceed(oldRequest)
//        }
//        val isVersionUpdate = oldHttpUrl.toString().contains("version_pkuhole_android")
//        if (!isVersionUpdate) {
//            return chain.proceed(oldRequest)
//        }
//        if(!isNewLogin){
//            return chain.proceed(oldRequest)
//        }
//        val isNewLogin = oldHttpUrl.toString().contains("api/encrypt_login")
        val isNewLogin = true
        if(!isNewLogin){
            return chain.proceed(oldRequest)
        }
        val newBaseUrl = TEST_HOST_ADDRESS.toHttpUrl()
        val newUrlBuilder = oldHttpUrl
            .newBuilder()
            .scheme(newBaseUrl.scheme)
            .host(newBaseUrl.host)
            .port(newBaseUrl.port)
            .build()
        val newRequest = oldRequest
            .newBuilder()
            .url(newUrlBuilder)
            .build()
        return chain.proceed(newRequest)
    }
}