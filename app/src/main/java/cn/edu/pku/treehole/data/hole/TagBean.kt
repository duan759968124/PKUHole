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
@Entity(tableName = "tag_list_table")
data class TagBean(
    @field:SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "tid")
    var tid: Long,

    @field:SerializedName("tag_name")
    @ColumnInfo(name = "tag_name")
    var tag_name: String?,

    @field:SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    var created_at: String?,

    @field:SerializedName("update_at")
    @ColumnInfo(name = "update_at")
    var update_at: String?
    )