package cn.edu.pku.treehole.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 *
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */

@Parcelize
data class UserInfo(
    @field:SerializedName("uid")
    var uid: String,
    @field:SerializedName("name")
    var name: String?,
    @field:SerializedName("token")
    var token: String?,
    @field:SerializedName("user_token")
    var user_token: String?,
    @field:SerializedName("gender")
    var gender: String?,
    @field:SerializedName("userIdentity")
    var userIdentity: String?,
    @field:SerializedName("department")
    var department: String?,
    @field:SerializedName("birthDate")
    var birthDate: String?,
    @field:SerializedName("pkuhole_push")
    var pkuhole_push: Int?,
    @field:SerializedName("pkuhole_hide_content")
    var pkuhole_hide_content: Int?,
    @field:SerializedName("hasiOS")
    var hasiOS: Int?,
    @field:SerializedName("hasAndroid")
    var hasAndroid: Int?,
    @field:SerializedName("newmsgcount")
    var newMsgCount:String?,
    @field:SerializedName("isop_svcid")
    var isop_svcid: String?,
    @field:SerializedName("isop_authtime")
    var isop_authtime: Long?,
    @field:SerializedName("isop_validate_ts")
    var isop_validate_ts: Long?,
    @field:SerializedName("action_remaining")
    var action_remaining: Long?,
    @field:SerializedName("jwt")
    var jwt: String
    ): Parcelable