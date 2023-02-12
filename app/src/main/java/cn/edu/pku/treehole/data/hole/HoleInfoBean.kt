package cn.edu.pku.treehole.data.hole

data class HoleInfoBean(
    var pid: Long,
    var holeItemBean: HoleItemBean,
//    var commentList: List<CommentItemBean>?,
    var commentItemBean1: CommentItemBean?,
    var commentItemBean2: CommentItemBean?,
)
