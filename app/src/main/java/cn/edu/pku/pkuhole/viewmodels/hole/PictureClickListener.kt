package cn.edu.pku.pkuhole.viewmodels.hole

import cn.edu.pku.pkuhole.data.hole.HoleItemBean

class PictureClickListener(val clickListener: (url: String) -> Unit) {
    fun onClick(holeItem: HoleItemBean) = holeItem.url?.let { clickListener(it) }
}