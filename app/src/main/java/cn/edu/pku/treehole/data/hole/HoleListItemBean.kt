package cn.edu.pku.treehole.data.hole

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 *
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 *
 * Hole list实体，网络获取实体
 */

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

    @field:SerializedName("anonymous")
    @ColumnInfo(name = "anonymous")
    var anonymous: Int?,

    @field:SerializedName("is_top")
    @ColumnInfo(name = "is_top")
    var is_top: Int?,

    @field:SerializedName("label")
    @ColumnInfo(name = "label")
    var label: Int?,

    @field:SerializedName("is_follow")
    @ColumnInfo(name = "is_follow")
    var is_follow: Int?,  //本人是否关注 1 是 ，0 未关注

    @field:SerializedName("label_info")
//    @ColumnInfo(name = "label_info_id")
//    var label_info_id: Int?,
    @Embedded(prefix = "label_info_")
    var label_info: TagBean?,

    @ColumnInfo(name = "isHole")
    var isHole: Int? = 1,

    @ColumnInfo(name = "isRead")
    var isRead: Int?,  // 0 or 1
)


fun List<HoleListItemBean>.asDatabaseBean():List<HoleItemBean>{
    return map{
        HoleItemBean(
            pid = it.pid,
            text = it.text,
            type = it.type,
            timestamp = it.timestamp,
            reply = it.reply,
            likenum = it.likenum,
            extra = it.extra,
            anonymous = it.anonymous,
            is_top = it.is_top,
            is_follow = it.is_follow,
            label = it.label,
            label_info = it.label_info,
            isHole = 1,
            tag = null,
            isRead = it.isRead,
        )
    }
}

fun HoleListItemBean.asDatabaseBean():HoleItemBean{
    return let{
        HoleItemBean(
            pid = it.pid,
            text = it.text,
            type = it.type,
            timestamp = it.timestamp,
            reply = it.reply,
            likenum = it.likenum,
            extra = it.extra,
            anonymous = it.anonymous,
            is_top = it.is_top,
            is_follow = it.is_follow,
            label = it.label,
            label_info = it.label_info,
            isHole = 1,
            tag = null,
            isRead = it.isRead,
        )
    }
}