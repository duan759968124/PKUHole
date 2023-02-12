package cn.edu.pku.treehole.data.hole

/**
 *
 * @Time:           2023/2/9
 * @Desc:
 * @Version:        1.0
 */
sealed class RecyclerDataModel {
    data class SealedHoleItemBean(
        var pid: Long,
        var text: String?,
        var type: String,
        var timestamp: Long,
        var reply: Int,
        var likenum: Int,
        var is_top: Int?,
        var is_follow: Int?,
        var label: Int?,
        var label_info: TagBean?,
        var tag: String?,
        var isHole: Int?,
        var isRead: Int?,  // 0 or 1
    ):RecyclerDataModel()

    data class SealedCommentItemBean(
        var cid: Long,
        var pid: Long,
        var text: String?,
        var timestamp: Long,
        var tag: String?,
        var islz: Int,
        var name: String,
        var randomH: Double,
    ):RecyclerDataModel()
}