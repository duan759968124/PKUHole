package cn.edu.pku.treehole.data.hole

import com.google.gson.annotations.SerializedName

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2022/7/26
 * @Desc:
 * @Version:        1.0
 */
data class LabelInfoBean(
    @field:SerializedName("id")
    var id: Int,

    @field:SerializedName("tag_name")
    var tag_name: String?,

    @field:SerializedName("created_at")
    var created_at: String?,

    @field:SerializedName("update_at")
    var update_at: String?
    )