package cn.edu.pku.pkuhole.api.interceptor

import cn.edu.pku.pkuhole.utilities.HOLE_UPDATE_HOST_ADDRESS
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
        val isVersionUpdate = oldHttpUrl.toString().contains("version_pkuhole_android")
        if (!isVersionUpdate) {
            return chain.proceed(oldRequest)
        }
//        if(!isNewLogin){
//            return chain.proceed(oldRequest)
//        }
//        val isNewLogin = oldHttpUrl.toString().contains("authen/login")
//        if(!isNewLogin){
//            return chain.proceed(oldRequest)
//        }
        // 可能需要向PKUAndroid一样添加headerValues属性值？？需要向多个服务器处理的链接比较多的情况
//        val newBaseUrl = ISOP_HOST_ADDRESS.toHttpUrl()
//        val newBaseUrl = HOLE_HOST_ADDRESS.toHttpUrl()
        val newBaseUrl = HOLE_UPDATE_HOST_ADDRESS.toHttpUrl()
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