package cn.edu.pku.treehole.viewmodels.hole

import cn.edu.pku.treehole.data.hole.HoleItemBean

class PictureClickListener(val clickListener: (url: String) -> Unit) {
    fun onClick(holeItem: HoleItemBean) = holeItem.url?.let { clickListener(it) }
}