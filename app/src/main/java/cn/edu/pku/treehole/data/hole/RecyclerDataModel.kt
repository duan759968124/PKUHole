package cn.edu.pku.treehole.data.hole

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2023/2/9
 * @Desc:
 * @Version:        1.0
 */
sealed class RecyclerDataModel {
    data class HoleItemBean(
        var pid: Long,
        var text: String?,
        var type: String,
        var timestamp: Long,
        var reply: Int,
        var likenum: Int,
        var is_top: Int?,
        var is_follow: Int?,
        var label: Int?,
    ):RecyclerDataModel()
}