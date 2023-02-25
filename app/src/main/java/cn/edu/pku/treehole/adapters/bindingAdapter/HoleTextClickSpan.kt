package cn.edu.pku.treehole.adapters.bindingAdapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import cn.edu.pku.treehole.NavigationDirections
import cn.edu.pku.treehole.R
import timber.log.Timber
import java.security.AccessController.getContext

/**
 *
 * @Time:           2022/3/9
 * @Desc:
 * @Version:        1.0
 */

object HoleNumberLinkHelper {
    class HoleTextClickSpan(var pid: String) : ClickableSpan() {
        override fun onClick(widget: View) {
            Timber.e("click Hole Number: $pid")
            widget.findNavController()
                .navigate(NavigationDirections.actionGlobalNavHoleDetail(pid.toLong()))
//        }
        }

        override fun updateDrawState(ds: TextPaint) {


            super.updateDrawState(ds)
//            ds.color = Color.MAGENTA
            ds.color = Color.parseColor("#437BCE")
            ds.isUnderlineText = true
        }
    }

    // 正则hole text内容
    fun regexHoleText(text: String): HashMap<String, IntRange> {
        //设置正则获取多个树洞号
        val holeNumberMap = HashMap<String, IntRange>()
//        val holeNumberPattern = "#[0-9]+".toRegex()
        val holeNumberPattern = "[1-9]\\d{5,6}(?!\\d)".toRegex()
        val found = holeNumberPattern.findAll(text)
        found.forEach { f ->
            val m = f.value
            val idx = f.range
            // 截取的数字串从index=0开始或者前一位不是数字
            if(idx.first==0 || !text[idx.first-1].isDigit()){
                holeNumberMap[m] = idx
            }

        }
        return holeNumberMap
    }
}

//object HoleNormalTextHelper {
//    class HoleNormalTextClickSpan : ClickableSpan() {
//        var clickListener: OnClickListener? = null
//
//        interface OnClickListener {
//            fun onSpanClick(widget: View)
//        }
//
//        override fun onClick(widget: View) {
//            Timber.e("click normal text ")
//            clickListener?.onSpanClick(widget)
//        }
//
//        override fun updateDrawState(ds: TextPaint) {
//            super.updateDrawState(ds)
//            ds.color = Color.GREEN
//            ds.isUnderlineText = false
//        }
//    }
//
//    fun setHoleNormalTextClickListener(spanned: Spanned, listener: HoleNormalTextClickSpan.OnClickListener?) {
//        spanned.getSpans(0, spanned.length, HoleNormalTextClickSpan::class.java)
//            .forEach { it.clickListener = listener }
//    }
//}

//@BindingAdapter(value = ["binding_hole_text", "binding_hole_number_listener"], requireAll = true)
//fun TextView.setHandleHoleText(holeText: String?, listener: HoleNumberLinkHelper.HoleTextClickSpan.OnClickListener?){
@BindingAdapter(value = ["binding_hole_text"], requireAll = true)
fun TextView.setHandleHoleText(holeText: String?) {
    if (holeText != null) {
        if (holeText.isNotEmpty()) {
            val spannableString = SpannableString(holeText)
            //正则查找所有的树洞号，并标记下来，设置为可点击
            val holeNumMap = HoleNumberLinkHelper.regexHoleText(holeText)
//            var lastStartIndex = 0
            holeNumMap.forEach { (value, indexRange) ->
//                if(indexRange.first != 0 && lastStartIndex == 0){
//                    spannableString.setSpan(HoleNormalTextHelper.HoleNormalTextClickSpan(),
//                        lastStartIndex,
//                        indexRange.first - 1,
//                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                }
                val pid = value.substring(0)
                Timber.e("$pid  $indexRange")
                spannableString.setSpan(HoleNumberLinkHelper.HoleTextClickSpan(pid),
                    indexRange.first,
                    indexRange.last + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                lastStartIndex = indexRange.last + 1
            }
//            if(lastStartIndex != spannableString.lastIndex){
//                spannableString.setSpan(HoleNormalTextHelper.HoleNormalTextClickSpan(),
//                    lastStartIndex,
//                    spannableString.length,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//            }
            text = spannableString
            movementMethod = LinkMovementMethod()
        }
    }
}

