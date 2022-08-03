package cn.edu.pku.treehole.data.hole

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 *
 * Hole list body
 */

data class HoleListBody<T>(
    @field:SerializedName("current_page")
    var current_page: Long?,

    @field:SerializedName("data")
    var data: List<T>?,

    @field:SerializedName("first_page_url")
    var first_page_url: String?,

    @field:SerializedName("from")
    var from: Long?,

    @field:SerializedName("next_page_url")
    var next_page_url: String?,

    @field:SerializedName("last_page_url")
    var last_page_url: String?,

    @field:SerializedName("last_page")
    var last_page: Long?,

    @field:SerializedName("prev_page_url")
    var prev_page_url: String?,

    @field:SerializedName("path")
    var path: String?,

    @field:SerializedName("per_page")
    var per_page: String?,

    @field:SerializedName("to")
    var to: Long?,

    @field:SerializedName("total")
    var total: Long?,
)
