package cn.edu.pku.pkuhole.api

import cn.edu.pku.pkuhole.BuildConfig
import cn.edu.pku.pkuhole.api.interceptor.AddHeaderInterceptor
import cn.edu.pku.pkuhole.api.interceptor.ChangeBaseUrlInterceptor
import cn.edu.pku.pkuhole.api.interceptor.LocalCookieJar
import cn.edu.pku.pkuhole.data.hole.AttentionItemBean
import cn.edu.pku.pkuhole.data.hole.CommentItemBean
import cn.edu.pku.pkuhole.data.hole.HoleItemModel
import cn.edu.pku.pkuhole.data.hole.HoleListItemBean
import cn.edu.pku.pkuhole.utilities.HOLE_HOST_ADDRESS
import cn.edu.pku.pkuhole.utilities.HTTP_TIMEOUT_CONNECT
import cn.edu.pku.pkuhole.utilities.HTTP_TIMEOUT_READ
import cn.edu.pku.pkuhole.utilities.Hole_NEW_HOST_ADDRESS
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface HoleApiService {


    // login
    @Deprecated("please using loginsecure")
    @FormUrlEncoded
    @POST("services/authen/login.php")
    suspend fun login(
        @Field("uid") uid: String,
        @Field("password") password: String,
    ): HoleApiResponse<String?>

    // login
    // password 先进行加密，之后url encoding，最后作为password提交
    @FormUrlEncoded
    @POST("services/authen/loginsecure.php")
    suspend fun loginsecure(
        @Field("uid") uid: String,
        @Field("password") password: String,
    ): HoleApiResponse<String?>

    // 获取树洞数据，按照页（p）
//    @GET("services/pkuhole/api.php")
//    suspend fun getHoleList(
//        @Query("action") action: String = "getlist",
//        @Query("user_token") userToken: String = "09ek80oc8hdj847ul0843nx58qolzw0l",
//        @Query("p") page: Int,
////        @Query("token") token: String = TEST_TOKEN
//    ): HoleApiResponse<List<HoleListItemBean>>

    // 获取树洞数据，按照页（p）
    @FormUrlEncoded
    @POST("services/hole/api.php")
    suspend fun getHoleList(
        @Field("action") action: String = "getlist",
        @Field("token") token: String,
        @Field("p") page: Int,
    ): HoleApiResponse<List<HoleListItemBean>?>


//    // 刷新树洞数据
//    @GET("services/pkuhole/api.php")
//    suspend fun refreshHoleList(
//        @Query("action") action: String = "refreshlist",
//        @Query("user_token") userToken: String = "09ek80oc8hdj847ul0843nx58qolzw0l",
//        @Query("timestamp") timestamp : Long,
////        @Query("token") token: String = TEST_TOKEN
//    ): HoleApiResponse<List<HoleListItemBean>?>

    // 刷新树洞数据
    @FormUrlEncoded
    @POST("services/hole/api.php")
    suspend fun refreshHoleList(
        @Field("action") action: String = "refreshlist",
        @Field("token") token: String,
        @Field("timestamp") timestamp : Long,
    ): HoleApiResponse<List<HoleListItemBean>?>

//    // 获取关注数据
//    @GET("services/pkuhole/api.php")
//    suspend fun getAttentionList(
//        @Query("action") action: String = "getattention",
//        @Query("user_token") userToken: String = "09ek80oc8hdj847ul0843nx58qolzw0l",
////        @Query("token") token: String = TEST_TOKEN
//    ): HoleApiResponse<List<AttentionItemBean>>

    // 获取关注数据
    @FormUrlEncoded
    @POST("services/hole/api.php")
    suspend fun getAttentionList(
        @Field("action") action: String = "getattention",
        @Field("token") token: String,
    ): HoleApiResponse<List<AttentionItemBean>?>


    // 获取一条树洞数据
//    @GET("services/pkuhole/api.php")
//    suspend fun getOneHole(
//        @Query("action") action: String = "getone",
//        @Query("user_token") userToken: String = "09ek80oc8hdj847ul0843nx58qolzw0l",
//        @Query("pid") pid: Long,
////        @Query("token") token: String = TEST_TOKEN
//    ): HoleApiResponse<HoleItemModel>

    // 获取一条树洞数据
    @FormUrlEncoded
    @POST("services/hole/api.php")
    suspend fun getOneHole(
        @Field("action") action: String = "getone",
        @Field("token") token: String,
        @Field("pid") pid: Long
    ): HoleApiResponse<HoleItemModel?>


    // 搜索
    @FormUrlEncoded
    @POST("services/hole/api.php")
    suspend fun search(
        @Field("action") action: String = "search",
        @Field("token") token: String,
        @Field("keywords") keywords: String
    ): HoleApiResponse<List<HoleItemModel>?>

    // 获取某条树洞下的评论列表
//    @GET("services/pkuhole/api.php")
//    suspend fun getCommentList(
//        @Query("action") action: String = "getcomment",
//        @Query("user_token") userToken: String = "09ek80oc8hdj847ul0843nx58qolzw0l",
//        @Query("pid") pid: Long,
//    ): HoleApiResponse<List<CommentItemBean>>

    // 获取某条树洞下的评论列表
    @FormUrlEncoded
    @POST("services/hole/api.php")
    suspend fun getCommentList(
        @Field("action") action: String = "getcomment",
        @Field("token") token: String,
        @Field("pid") pid: Long,
    ): HoleApiResponse<List<CommentItemBean>?>

//    // 回复评论
//    @FormUrlEncoded
//    @POST("services/pkuhole/api.php")
//    suspend fun sendReplyComment(
//        @Query("action") action: String = "docomment",
//        @Field("user_token") userToken: String = "09ek80oc8hdj847ul0843nx58qolzw0l",
//        @Field("pid") pid: Long,
//        @Field("text") text: String,
//    ): HoleApiResponse<Long>

    // 回复评论
    @FormUrlEncoded
    @POST("services/hole/api.php")
    suspend fun sendReplyComment(
        @Field("action") action: String = "docomment",
        @Field("token") token: String,
        @Field("pid") pid: Long,
        @Field("text") text: String,
    ): HoleApiResponse<Long?>

    // Attention状态变化【关注或者取消关注】
    @FormUrlEncoded
    @POST("services/hole/api.php")
    suspend fun switchAttentionStatus(
        @Field("action") action: String = "attention",
        @Field("token") token: String,
        @Field("pid") pid: Long,
        @Field("switch") switch: Int,
    ): HoleApiResponse<String?>


    // 举报树洞
    @FormUrlEncoded
    @POST("services/hole/api.php")
    suspend fun report(
        @Field("action") action: String = "report",
        @Field("token") token: String,
        @Field("pid") pid: Long,
        @Field("reason") reason: String,
    ): HoleApiResponse<String?>

    // 发树洞[带图片]
    @FormUrlEncoded
    @POST("services/hole/api.php")
    suspend fun postHoleWithImage(
        @Field("action") action: String = "dopost",
        @Field("token") token: String,
        @Field("type") type: String = "image",
        @Field("text") text: String,
        @Field("data") data: String,
        @Field("length") length: Int = 0,
    ): HoleApiResponse<Long?>

    // 发树洞[带图片]
    @FormUrlEncoded
    @POST("services/hole/api.php")
    suspend fun postHoleOnlyText(
        @Field("action") action: String = "dopost",
        @Field("token") token: String,
        @Field("type") type: String = "text",
        @Field("text") text: String,
        @Field("length") length: Int = 0,
    ): HoleApiResponse<Long?>



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
//                .addInterceptor(ChangeBaseUrlInterceptor())
                .addInterceptor(AddHeaderInterceptor())
                .addInterceptor(logger)
                .cookieJar(LocalCookieJar())
                .connectTimeout(HTTP_TIMEOUT_CONNECT, TimeUnit.SECONDS)
                .readTimeout(HTTP_TIMEOUT_READ, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(Hole_NEW_HOST_ADDRESS)
                .client(okHttpclient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HoleApiService::class.java)
        }
    }
}