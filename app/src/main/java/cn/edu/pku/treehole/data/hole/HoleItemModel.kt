package cn.edu.pku.treehole.data.hole

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * 最小的实体
 */
data class HoleItemModel(

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

    @field:SerializedName("is_follow")
    @ColumnInfo(name = "is_follow")
    var is_follow: Int?,

    @field:SerializedName("label_info")
    @Embedded(prefix = "label_info_")
    var label_info: TagBean?,

    @field:SerializedName("label")
    @ColumnInfo(name = "label")
    var label: Int?,

    @ColumnInfo(name = "isRead")
    var isRead: Int?,  // 0 or 1

)


fun HoleItemModel.asDatabaseBean():HoleItemBean{
    return let{
        HoleItemBean(
            pid = it.pid,
            text = it.text,
            type = it.type,
            timestamp = it.timestamp,
            reply = it.reply,
            likenum = it.likenum,
            extra = it.extra,
            tag = null,
            isHole = 0,
            is_top = it.is_top,
            is_follow = it.is_follow,
            anonymous = it.anonymous,
            label = it.label,
            isRead = it.isRead,
            label_info = it.label_info
//            label_info = null,
        )
    }
}

//fun HoleItemModel.asDatabaseAttentionBean():AttentionItemBean{
//    return let{
//        AttentionItemBean(
//            pid = it.pid,
//            text = it.text,
//            type = it.type,
//            timestamp = it.timestamp,
//            reply = it.reply,
//            likenum = it.likenum,
//            extra = it.extra,
//            url = it.url,
//            tag = it.tag,
//            attention_tag = null
//        )
//    }
//}
