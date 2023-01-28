package cn.edu.pku.treehole.data

import com.google.gson.annotations.SerializedName

/**
 *
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */

data class EmptyBean(
    @field:SerializedName("empty_data")
    var empty_data: String?,
)