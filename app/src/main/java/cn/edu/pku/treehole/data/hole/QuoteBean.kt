package cn.edu.pku.treehole.data.hole

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 *
 * @Time:           2022/7/26
 * @Desc:
 * @Version:        1.0
 *
 * label实体，同时是数据库实体
 */

data class QuoteBean(
    @field:SerializedName("pid")
    var pid: Long,
    @field:SerializedName("text")
    var text: String?,
    @field:SerializedName("name_tag")
    var name_tag: String?,
    @field:SerializedName("name")
    var name: String?
    )