package cn.edu.pku.treehole.data.hole

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

/**
 *
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 *
 * attention 实体 网络获取实体
 */
data class AttentionItemBean(
    @field:SerializedName("pid")
    var pid: Long,

    @field:SerializedName("text")
    var text: String?,

    @field:SerializedName("type")
    var type: String,

//    Todo: 时间戳类型？？转化和格式
    @field:SerializedName("timestamp")
    var timestamp: Long,

    @field:SerializedName("reply")
    var reply: Int,

    @field:SerializedName("likenum")
    var likenum: Int,

    @field:SerializedName("extra")
    var extra: Int?,


    @field:SerializedName("tag")
    @ColumnInfo(name = "tag")
    var tag: String?,

    @field:SerializedName("label_info")
////    @ColumnInfo(name = "label_info_id")
////    var label_info_id: Int?,
    @Embedded(prefix = "label_info_")
    var label_info: TagBean?,

    @field:SerializedName("is_follow")
    @ColumnInfo(name = "is_follow")
    var is_follow: Int? = 1,

)



fun List<AttentionItemBean>.asDatabaseBean():List<HoleItemBean>{
    return map{
        HoleItemBean(
            pid = it.pid,
            text = it.text,
            type = it.type,
            timestamp = it.timestamp,
            reply = it.reply,
            likenum = it.likenum,
            extra = it.extra,
            tag = it.tag,
            is_top = null,
            isHole = 0,
            is_follow = it.is_follow,
            anonymous = null,
            label = null,
            isRead = null,
            label_info = it.label_info,
        )
    }
}

fun AttentionItemBean.asDatabaseBean():HoleItemBean{
    return let{
        HoleItemBean(
            pid = it.pid,
            text = it.text,
            type = it.type,
            timestamp = it.timestamp,
            reply = it.reply,
            likenum = it.likenum,
            extra = it.extra,
            tag = it.tag,
            is_top = null,
            isHole = 0,
            is_follow = it.is_follow,
            anonymous = null,
//            label_info = null,
            label = null,
            isRead = null,
            label_info = it.label_info
        )
    }
}