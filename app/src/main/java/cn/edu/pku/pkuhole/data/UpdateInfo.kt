package cn.edu.pku.pkuhole.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */

@Parcelize
data class UpdateInfo(
    @field:SerializedName("app_version_code")
    var app_version_code: Int,
    @field:SerializedName("app_version_name")
    var app_version_name: String?,
    @field:SerializedName("app_file_url")
    var app_file_url: String?,
    @field:SerializedName("update_log")
    var update_log: String?
    ): Parcelable