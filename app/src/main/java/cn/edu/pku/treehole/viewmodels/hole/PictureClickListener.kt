package cn.edu.pku.treehole.viewmodels.hole

import cn.edu.pku.treehole.data.hole.HoleInfoBean
import cn.edu.pku.treehole.data.hole.HoleItemBean

class PictureClickListener(val clickListener: (holeItem: HoleItemBean) -> Unit) {
    fun onClick(holeItem: HoleItemBean) = clickListener(holeItem)
}

class PictureClickListener2(val clickListener: (holeItem: HoleInfoBean) -> Unit) {
    fun onClick(holeItem: HoleInfoBean) = clickListener(holeItem)
}