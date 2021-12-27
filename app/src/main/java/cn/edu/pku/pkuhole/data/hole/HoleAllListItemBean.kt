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
@Entity(tableName = "hole_all_list_table")
data class HoleAllListItemBean(
    @field:SerializedName("pid")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pid")
    var pid: Long,

    @field:SerializedName("hidden")
    @ColumnInfo(name = "hidden")
    var hidden: Int = 0,

    @field:SerializedName("text")
    @ColumnInfo(name = "text")
    var text: String? = "",

    @field:SerializedName("type")
    @ColumnInfo(name = "type")
    var type: String = "",

//    Todo: 时间戳类型？？转化和格式
    @field:SerializedName("timestamp")
    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0L,

    @field:SerializedName("reply")
    @ColumnInfo(name = "reply")
    var reply: Int = 0,

    @field:SerializedName("likenum")
    @ColumnInfo(name = "likenum")
    var likenum: Int = 0,

    @field:SerializedName("extra")
    @ColumnInfo(name = "extra")
    var extra: Int = 0,

    @field:SerializedName("url")
    @ColumnInfo(name = "url")
    var url: String? = "",

    @field:SerializedName("hot")
    @ColumnInfo(name = "hot")
    var hot: Long = 0L,

    @field:SerializedName("tag")
    @ColumnInfo(name = "tag")
    var tag: String?
)