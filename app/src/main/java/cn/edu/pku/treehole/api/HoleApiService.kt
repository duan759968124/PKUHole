package cn.edu.pku.treehole.api

import cn.edu.pku.treehole.BuildConfig
import cn.edu.pku.treehole.api.interceptor.AddHeaderInterceptor
import cn.edu.pku.treehole.api.interceptor.ChangeBaseUrlInterceptor
import cn.edu.pku.treehole.api.interceptor.LocalCookieJar
import cn.edu.pku.treehole.data.EmptyBean
import cn.edu.pku.treehole.data.HoleManagementPracticeBean
import cn.edu.pku.treehole.data.UpdateInfo
import cn.edu.pku.treehole.data.UserInfo
import cn.edu.pku.treehole.data.hole.*
import cn.edu.pku.treehole.utilities.HOLE_HOST_ADDRESS
import cn.edu.pku.treehole.utilities.HTTP_TIMEOUT_CONNECT
import cn.edu.pku.treehole.utilities.HTTP_TIMEOUT_READ
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface HoleApiService {

    // check update
    @GET("pku_hole_app/version_pkuhole_android.txt")
    suspend fun checkUpdate(
    ): HoleApiResponse<UpdateInfo?>

    // login
    // password 先进行加密，之后url encoding，最后作为password提交
    @FormUrlEncoded
    @POST("api/encrypt_login")
    suspend fun loginSecure(
        @Field("uid") uid: String,
        @Field("password") password: String,
        @Field("agreement") agreement: Int = 1,
    ): HoleApiResponse<UserInfo?>


    // 发送短信验证码
    @FormUrlEncoded
    @POST("api/jwt_send_msg")
    suspend fun sendSMSValidCode(
        @Field("empty_param") empty_param: String = "",
    ): HoleApiResponse<EmptyBean?>

    // 短信验证
    @FormUrlEncoded
    @POST("api/jwt_msg_verify")
    suspend fun verifySMSValidCode(
        @Field("valid_code") valid_code: String,
    ): HoleApiResponse<EmptyBean?>

    // 随机显示树洞管理规范
    @GET("api/pku/manager_spec")
    suspend fun getRandomHoleManagementPractice(
    ): HoleApiResponse<HoleManagementPracticeBean?>


    // 获取树洞数据，按照页（p）
    @GET("api/pku_hole")
    suspend fun getHoleList(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 10,
    ): HoleApiResponse<HoleListBody<HoleListItemBean>?>


    // 获取图片
    @GET("api/pku_image/{pid}")
    suspend fun getPictureFromPid(
        @Path("pid") pid: Long,
    ): HoleApiResponse<String?>


//    // 刷新树洞数据
//    @GET("services/pkuhole/api.php")
//    suspend fun refreshHoleList(
//        @Query("action") action: String = "refreshlist",
//        @Query("user_token") userToken: String = "09ek80oc8hdj847ul0843nx58qolzw0l",
//        @Query("timestamp") timestamp : Long,
////        @Query("token") token: String = TEST_TOKEN
//    ): HoleApiResponse<List<HoleListItemBean>?>

    // 刷新树洞数据
//    @FormUrlEncoded
//    @POST("services/hole/api.php")
//    suspend fun refreshHoleList(
//        @Field("action") action: String = "refreshlist",
//        @Field("token") token: String,
//        @Field("timestamp") timestamp : Long,
//    ): HoleApiResponse<List<HoleListItemBean>?>

    // 刷新树洞数据
    @GET("api/pku_hold_refresh")
    suspend fun refreshHoleList(
        @Query("timestamp") timestamp: Long,
    ): HoleApiResponse<List<HoleListItemBean>?>

//    // 获取关注数据
//    @GET("services/pkuhole/api.php")
//    suspend fun getAttentionList(
//        @Query("action") action: String = "getattention",
//        @Query("user_token") userToken: String = "09ek80oc8hdj847ul0843nx58qolzw0l",
////        @Query("token") token: String = TEST_TOKEN
//    ): HoleApiResponse<List<AttentionItemBean>>

    // 获取关注数据
//    @FormUrlEncoded
//    @POST("services/hole/api.php")
//    suspend fun getAttentionList(
//        @Field("action") action: String = "getattention",
//        @Field("token") token: String,
//    ): HoleApiResponse<List<AttentionItemBean>?>

    // 获取关注数据
    @GET("api/follow")
    suspend fun getAttentionList(
        @Query("limit") limit: Int = 10000
    ): HoleApiResponse<HoleListBody<AttentionItemBean>?>


    // 获取一条树洞数据
//    @GET("services/pkuhole/api.php")
//    suspend fun getOneHole(
//        @Query("action") action: String = "getone",
//        @Query("user_token") userToken: String = "09ek80oc8hdj847ul0843nx58qolzw0l",
//        @Query("pid") pid: Long,
////        @Query("token") token: String = TEST_TOKEN
//    ): HoleApiResponse<HoleItemModel>

//    // 获取一条树洞数据
//    @FormUrlEncoded
//    @POST("services/hole/api.php")
//    suspend fun getOneHole(
//        @Field("action") action: String = "getone",
//        @Field("token") token: String,
//        @Field("pid") pid: Long
//    ): HoleApiResponse<HoleItemModel?>
    // 获取一条树洞数据
    @GET("api/pku/{pid}")
    suspend fun getOneHole(
    @Path("pid") pid: Long
    ): HoleApiResponse<HoleItemModel?>


    // 搜索
//    @FormUrlEncoded
//    @POST("services/hole/api.php")
//    suspend fun search(
//        @Field("action") action: String = "search",
//        @Field("token") token: String,
//        @Field("keywords") keywords: String
//    ): HoleApiResponse<List<HoleItemModel>?>
    @GET("api/pku_hole")
    suspend fun search(
        @Query("page") page: Long,
        @Query("keyword") keywords: String,
        @Query("label") labelId: Long?,
    ): HoleApiResponse<HoleListBody<HoleItemModel>?>

    @GET("api/pku_hole")
    suspend fun searchPid(
        @Query("pid") pid: String
    ): HoleApiResponse<HoleListBody<HoleItemModel>?>

    // 获取某条树洞下的评论列表
//    @GET("services/pkuhole/api.php")
//    suspend fun getCommentList(
//        @Query("action") action: String = "getcomment",
//        @Query("user_token") userToken: String = "09ek80oc8hdj847ul0843nx58qolzw0l",
//        @Query("pid") pid: Long,
//    ): HoleApiResponse<List<CommentItemBean>>

    // 获取某条树洞下的评论列表
//    @FormUrlEncoded
//    @POST("services/hole/api.php")
//    suspend fun getCommentList(
//        @Field("action") action: String = "getcomment",
//        @Field("token") token: String,
//        @Field("pid") pid: Long,
//    ): HoleApiResponse<List<CommentItemBean>?>

    @GET("api/pku_comment/{pid}")
    suspend fun getCommentList(
        @Path("pid") pid: Long,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 100000,
    ): HoleApiResponse<HoleListBody<CommentItemBean>?>

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
    @POST("/api/pku_comment")
    suspend fun sendReplyComment(
        @Field("pid") pid: Long,
        @Field("text") text: String,
    ): HoleApiResponse<EmptyBean?>

    // Attention状态变化【关注或者取消关注】
    @FormUrlEncoded
    @POST("api/pku_attention/{pid}")
    suspend fun switchAttentionStatus(
        @Path("pid") pid: Long,
        @Field("empty_param") empty_param: String = "",
    ): HoleApiResponse<String?>


    // 举报树洞
    @FormUrlEncoded
    @POST("api/pku_report/{pid}")
    suspend fun report(
        @Path("pid") pid: Long,
        @Field("reason") reason: String,
    ): HoleApiResponse<EmptyBean?>

    // 发树洞[带图片]
    @FormUrlEncoded
    @POST("api/pku_store")
    suspend fun postHoleWithImage(
        @Field("type") type: String = "image",
        @Field("text") text: String,
        @Field("data") data: String,
        @Field("data_type") data_type: String = "base64",
        @Field("label") labelId: Long?
    ): HoleApiResponse<EmptyBean?>

    // 发树洞[不带图片]
    @FormUrlEncoded
    @POST("api/pku_store")
    suspend fun postHoleOnlyText(
        @Field("type") type: String = "text",
        @Field("text") text: String,
        @Field("label") labelId: Long?
    ): HoleApiResponse<EmptyBean?>

    // 标签列表
    @GET("api/pku/tags")
    suspend fun getTagList(
    ): HoleApiResponse<List<TagBean>?>




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