package cn.edu.pku.pkuhole.adapters.bindingAdapter

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import cn.edu.pku.pkuhole.R
import timber.log.Timber

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2022/3/9
 * @Desc:
 * @Version:        1.0
 */
class HoleTextClickSpan(var pid: String, var listener: OnClickListener? = null): ClickableSpan() {

    interface OnClickListener{
        fun onSpanClick(widget: View, pid: String)
    }
    override fun onClick(widget: View) {
        Timber.e("click Hole Number: $pid")
        listener?.onSpanClick(widget, pid)
    }

    @SuppressLint("ResourceAsColor")
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = R.color.hole_blue
        ds.isUnderlineText = false
    }
}

// 正则hole text内容
fun regexHoleText(text: String): HashMap<String, IntRange>{
    //设置正则获取多个树洞号
    val holeNumberMap = HashMap<String, IntRange>()
    val holeNumberPattern = "#[0-9]+".toRegex()
    val found = holeNumberPattern.findAll(text)
    found.forEach { f ->
        val m = f.value
        val idx = f.range
        holeNumberMap[m] = idx
    }
    return holeNumberMap
}


@BindingAdapter(value = ["binding_hole_text", "binding_hole_number_listener"], requireAll = true)
fun TextView.setHandleHoleText(holeText: String?, listener: HoleTextClickSpan.OnClickListener){
    if (holeText != null) {
        if(holeText.isNotEmpty()){
            val spannableString = SpannableString(holeText)
            //正则查找所有的树洞号，并标记下来，设置为可点击
            val holeNumMap = regexHoleText(holeText)
            holeNumMap.forEach { (value, indexRange) ->
                val pid = value.substring(1)
                Timber.e("$pid  $indexRange")
                spannableString.setSpan(HoleTextClickSpan(pid, listener), indexRange.first, indexRange.last + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            spannableString.apply {  }
            text = spannableString
            movementMethod = LinkMovementMethod()

        }
    }
}

