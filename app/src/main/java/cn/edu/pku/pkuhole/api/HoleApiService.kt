package cn.edu.pku.pkuhole.api

import cn.edu.pku.pkuhole.BuildConfig
import cn.edu.pku.pkuhole.api.interceptor.AddHeaderInterceptor
import cn.edu.pku.pkuhole.api.interceptor.ChangeBaseUrlInterceptor
import cn.edu.pku.pkuhole.api.interceptor.LocalCookieJar
import cn.edu.pku.pkuhole.data.hole.HoleListItemBean
import cn.edu.pku.pkuhole.utilities.HOLE_HOST_ADDRESS
import cn.edu.pku.pkuhole.utilities.HTTP_TIMEOUT_CONNECT
import cn.edu.pku.pkuhole.utilities.HTTP_TIMEOUT_READ
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface HoleApiService {

    // 获取树洞数据，按照页（p）
    @GET("services/pkuhole/api.php")
    suspend fun getHoleList(
        @Query("action") action: String = "getlist",
        @Query("user_token") userToken: String = "09ek80oc8hdj847ul0843nx58qolzw0l",
        @Query("p") page: Int,
//        @Query("token") token: String = TEST_TOKEN
    ): HoleApiResponse<List<HoleListItemBean>>

    // 刷新数据
    @GET("services/pkuhole/api.php")
    suspend fun refreshHoleList(
        @Query("action") action: String = "refreshlist",
        @Query("user_token") userToken: String = "09ek80oc8hdj847ul0843nx58qolzw0l",
        @Query("timestamp") timestamp : Long,
//        @Query("token") token: String = TEST_TOKEN
    ): HoleApiResponse<List<HoleListItemBean>>


    // 获取关注列表
    @GET("services/pkuhole/api.php")
    suspend fun getAttentionList(
        @Query("action") action: String = "getattention",
        @Query("user_token") userToken: String = "09ek80oc8hdj847ul0843nx58qolzw0l",
//        @Query("token") token: String = TEST_TOKEN
    ): HoleApiResponse<List<HoleListItemBean>>


    companion object{
        fun create() : HoleApiService {
            val logger = HttpLoggingInterceptor().setLevel(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            )
            val okHttpclient = OkHttpClient.Builder()
                .addInterceptor(ChangeBaseUrlInterceptor())
                .addInterceptor(AddHeaderInterceptor())
                .addInterceptor(logger)
                .cookieJar(LocalCookieJar())
                .connectTimeout(HTTP_TIMEOUT_CONNECT, TimeUnit.SECONDS)
                .readTimeout(HTTP_TIMEOUT_READ, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(HOLE_HOST_ADDRESS)
                .client(okHttpclient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HoleApiService::class.java)
        }
    }
}