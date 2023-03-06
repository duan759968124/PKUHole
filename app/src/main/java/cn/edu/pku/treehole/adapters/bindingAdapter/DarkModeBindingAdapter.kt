package cn.edu.pku.treehole.adapters.bindingAdapter

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.slider.Slider


/**
 *
 * @Time:           2023/01/13
 * @Desc:
 * @Version:        1.0
 */


@BindingAdapter("showSelectList")
fun bindShowSelectList(view: View, darkModeType: Int) {
    view.visibility = if (darkModeType == 0) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("showNormalModeCheckedIcon")
fun bindShowNormalModeCheckedIcon(view: View, darkModeType: Int) {
    view.visibility = if (darkModeType == 1) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("showDarkModeCheckedIcon")
fun bindShowDarkModeCheckedIcon(view: View, darkModeType: Int) {
    view.visibility = if (darkModeType == 2) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("setDarkModeText")
fun TextView.bindSetDarkModeText(darkModeType: Int) {
    when(darkModeType){
        0->{
            text = "跟随系统"
        }
        1->{
            text = "已关闭"
        }
        2->{
            text = "已开启"
        }
    }
}

@BindingAdapter("setQuoteCommentText")
fun TextView.bindSetQuoteCommentText(isShow: Boolean) {
    when(isShow){
        true->{
            text = "显示"
        }
        false->{
            text = "不显示"
        }
    }
}



