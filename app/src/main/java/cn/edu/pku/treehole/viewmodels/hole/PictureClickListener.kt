package cn.edu.pku.treehole.viewmodels.hole

import cn.edu.pku.treehole.data.hole.HoleItemBean

class PictureClickListener(val clickListener: (holeItem: HoleItemBean) -> Unit) {
    fun onClick(holeItem: HoleItemBean) = clickListener(holeItem)
}