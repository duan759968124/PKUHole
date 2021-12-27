package cn.edu.pku.pkuhole.data.hole

import com.google.gson.annotations.SerializedName

data class HoleApiResponse<T> (
    @field:SerializedName("code") val code: Int,
    @field:SerializedName("timestamp") val timestamp: Long?,
    @field:SerializedName("data") val data: T?,
    @field:SerializedName("count") val count : Int?,
    @field:SerializedName("attention") val attention: Int?,
    @field:SerializedName("captcha") val captcha: Boolean?,
)