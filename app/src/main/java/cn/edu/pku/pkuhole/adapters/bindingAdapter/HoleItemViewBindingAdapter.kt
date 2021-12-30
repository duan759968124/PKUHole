package cn.edu.pku.pkuhole.adapters.bindingAdapter

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import cn.edu.pku.pkuhole.data.hole.HoleListItemBean
import cn.edu.pku.pkuhole.utilities.convertDurationToFormatted


/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/30
 * @Desc:
 * @Version:        1.0
 */
@BindingAdapter("postDurationFormatted")
fun TextView.setPostDurationFormatted(item: HoleListItemBean){
    text = convertDurationToFormatted(item.timestamp, System.currentTimeMillis(), context.resources)
}

@BindingAdapter("hasImage")
fun bindHasImage(view: View, url: String?) {
    view.visibility = if (url == null || url == "") {
        View.GONE
    } else {
        View.VISIBLE
    }
}