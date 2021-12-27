package cn.edu.pku.pkuhole.data.hole

import com.google.gson.annotations.SerializedName

data class HoleApiAllListItem(
    @field:SerializedName("pid") val pid: Long,
    @field:SerializedName("hidden") val hidden: Int,
    @field:SerializedName("text") val text: String,
    @field:SerializedName("type") val type: String,
//    Todo: 时间戳类型？？转化和格式
    @field:SerializedName("timestamp") val timestamp: Long,
    @field:SerializedName("reply") val reply: Int,
    @field:SerializedName("likenum") val likenum: Int,
    @field:SerializedName("extra") val extra: Int,
    @field:SerializedName("url") val url: String,
    @field:SerializedName("hot") val hot: Long,
    @field:SerializedName("tag") val tag: String?
    )