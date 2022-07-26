package cn.edu.pku.treehole.data.hole

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
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

    @field:SerializedName("url")
    var url: String?,

//    // 树洞列表 单独属性
//    @field:SerializedName("hot")
//    @ColumnInfo(name = "hot")
//    var hot: Long?,
//
//    // 树洞列表 单独属性
//    @field:SerializedName("hidden")
//    @ColumnInfo(name = "hidden")
//    var hidden: Int?,

    @field:SerializedName("tag")
    @ColumnInfo(name = "tag")
    var tag: String?,

    // 关注列表 单独属性
    @field:SerializedName("attention_tag")
    @ColumnInfo(name = "attention_tag")
    var attention_tag: String?,

    @ColumnInfo(name = "isAttention")
    var isAttention: Int? = 1
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
            url = it.url,
            tag = it.tag,
            hot = null,
            hidden = null,
            attention_tag = it.attention_tag,
            isHole = 0,
            isAttention = 1
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
            url = it.url,
            tag = it.tag,
            hot = null,
            hidden = null,
            attention_tag = it.attention_tag,
            isHole = 0,
            isAttention = 1
        )
    }
}