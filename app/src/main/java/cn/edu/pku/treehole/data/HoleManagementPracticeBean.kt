package cn.edu.pku.treehole.data

import com.google.gson.annotations.SerializedName

/**
 *
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */

data class HoleManagementPracticeBean(
    @field:SerializedName("id")
    var id: Int?,
    @field:SerializedName("mark")
    var mark: String?,
    @field:SerializedName("desc")
    var desc: String?,
    @field:SerializedName("created_at")
    var created_at: String?,
    @field:SerializedName("updated_at")
    var updated_at: String?,
    @field:SerializedName("deleted_at")
    var deleted_at: String?,
)