package cn.edu.pku.treehole.api

import com.google.gson.annotations.SerializedName

data class HoleApiResponseOld<T> (
    @field:SerializedName("code") val code: Int = 0,
    @field:SerializedName("msg") val msg: String? = null,

    @field:SerializedName("timestamp") val timestamp: Long? = null,
    @field:SerializedName("data") val data: T? = null,
    @field:SerializedName("count") val count : Int? = null,
    @field:SerializedName("attention") val attention: Int? = null,
    @field:SerializedName("captcha") val captcha: Boolean? = null,

    @field:SerializedName("uid") val uid: String? = null,
    @field:SerializedName("name") val name: String? = null,
    @field:SerializedName("department") val department: String? = null,
    @field:SerializedName("token") val token: String? = null,
    @field:SerializedName("token_timestamp") val token_timestamp: Long? = null,

//    var dataState: DataState? = null,
    var error: Throwable? = null
)


//enum class DataState {
//    STATE_CREATE,//创建
//    STATE_LOADING,//加载中
//    STATE_SUCCESS,//成功
//    STATE_COMPLETED,//完成
//    STATE_EMPTY,//数据为null
//    STATE_FAILED,//接口请求成功但是服务器返回error
//    STATE_ERROR,//请求失败
//    STATE_UNKNOWN//未知
//}
