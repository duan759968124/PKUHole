package cn.edu.pku.treehole.data.hole

/**
 * 最小的实体
 */
data class HoleItemModel(

    var pid: Long,
    var text: String?,
    var type: String,
    var timestamp: Long,
    var reply: Int,
    var likenum: Int,
    var extra: Int?,
    var anonymous: Int?,
    var url: String?,
    var is_top: Int?,
    var label: Int?,
    var is_follow: Int?,
    var isRead: Int?,
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
            tag = null,
            isHole = 0,
            is_top = it.is_top,
            is_follow = it.is_follow,
            anonymous = it.anonymous,
            label = it.label,
            isRead = it.isRead
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
