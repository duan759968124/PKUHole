package cn.edu.pku.pkuhole.data.hole

/**
 * 最小的实体
 */
data class HoleItemModel (

    var pid: Long,
    var text: String?,
    var type: String,
    var timestamp: Long,
    var reply: Int,
    var likenum: Int,
    var extra: Int?,
    var url: String?,
    var tag: String?
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
            url = it.url,
            tag = it.tag,
            hot = null,
            hidden = null,
            attention_tag = null,
            isHole = null,
            isAttention = null
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
