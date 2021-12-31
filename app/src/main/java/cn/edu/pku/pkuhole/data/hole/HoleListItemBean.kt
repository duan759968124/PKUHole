package cn.edu.pku.pkuhole.data.hole

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */
@Entity(tableName = "hole_list_table")
data class HoleListItemBean(
    @field:SerializedName("pid")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pid")
    var pid: Long,

    @field:SerializedName("text")
    @ColumnInfo(name = "text")
    var text: String?,

    @field:SerializedName("type")
    @ColumnInfo(name = "type")
    var type: String,

//    Todo: 时间戳类型？？转化和格式
    @field:SerializedName("timestamp")
    @ColumnInfo(name = "timestamp")
    var timestamp: Long,

    @field:SerializedName("reply")
    @ColumnInfo(name = "reply")
    var reply: Int,

    @field:SerializedName("likenum")
    @ColumnInfo(name = "likenum")
    var likenum: Int,

    @field:SerializedName("extra")
    @ColumnInfo(name = "extra")
    var extra: Int?,

    @field:SerializedName("url")
    @ColumnInfo(name = "url")
    var url: String?,

    // 树洞列表 单独属性
    @field:SerializedName("hot")
    @ColumnInfo(name = "hot")
    var hot: Long?,

    // 树洞列表 单独属性
    @field:SerializedName("hidden")
    @ColumnInfo(name = "hidden")
    var hidden: Int?,

    @field:SerializedName("tag")
    @ColumnInfo(name = "tag")
    var tag: String?,

    // 关注列表 单独属性
    @field:SerializedName("attention_tag")
    @ColumnInfo(name = "attention_tag")
    var attention_tag: String? = "attention_tag",

    @ColumnInfo(name = "is_hole")
    var isHole : Boolean?,
    @ColumnInfo(name = "is_attention")
    var isAttention : Boolean?
)