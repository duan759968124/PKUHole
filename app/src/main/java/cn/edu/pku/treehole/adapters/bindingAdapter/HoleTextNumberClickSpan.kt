//package cn.edu.pku.pkuhole.adapters.bindingAdapter
//
//import android.graphics.Color
//import android.text.Spannable
//import android.text.SpannableString
//import android.text.Spanned
//import android.text.TextPaint
//import android.text.method.LinkMovementMethod
//import android.text.style.ClickableSpan
//import android.view.View
//import android.widget.TextView
//import androidx.databinding.BindingAdapter
//
//
///**
// *
// * @Author:         HuidongQin
// * @e-mail:         hdqin@pku.edu.cn
// * @Time:           2022/3/14
// * @Desc:
// * @Version:        1.0
// */
//
//class HoleTextNumberClickSpan(var pid: String) : ClickableSpan() {
//    var clickListener: OnClickListener? = null
//    interface OnClickListener {
//        fun onSpanClick(widget: View, pid: String)
//    }
//
//    override fun onClick(tv: View) {
//        clickListener?.onSpanClick(tv, pid)
//    }
//
//    override fun updateDrawState(ds: TextPaint) {
//        ds.color = Color.BLUE
//        ds.isUnderlineText = true // set to false to remove underline
//    }
//
//}
//
//// 正则hole text内容
//fun regexHoleText(text: String): HashMap<String, IntRange> {
//    //设置正则获取多个树洞号
//    val holeNumberMap = HashMap<String, IntRange>()
//    val holeNumberPattern = "#[0-9]+".toRegex()
//    val found = holeNumberPattern.findAll(text)
//    found.forEach { f ->
//        val m = f.value
//        val idx = f.range
//        holeNumberMap[m] = idx
//    }
//    return holeNumberMap
//}
//
///**
// * 设置点击事件。
// */
//fun setClickListener(spanned: Spanned, listener: HoleTextNumberClickSpan.OnClickListener?) {
//    spanned.getSpans(0, spanned.length, HoleTextNumberClickSpan::class.java)
//        .forEach { it.clickListener = listener }
//}
//
//
//
//@BindingAdapter(value = ["binding_hole_text", "binding_hole_number_listener"], requireAll = true)
//fun TextView.setHandleHoleText(holeText: String?, listener: HoleTextNumberClickSpan.OnClickListener?){
//    if (holeText != null) {
//        if(holeText.isNotEmpty()){
//            val spannableString = SpannableString(holeText)
//            //正则查找所有的树洞号，并标记下来，设置为可点击
//            val holeNumMap = regexHoleText(holeText)
//            holeNumMap.forEach { (value, indexRange) ->
//                val pid = value.substring(1)
//                spannableString.setSpan(HoleTextNumberClickSpan(pid), indexRange.first, indexRange.last + 1,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//            }
//            spannableString.apply {
//                if (listener != null) {
//                    setClickListener(this, listener)
//                }
//            }
//            text = spannableString
//            movementMethod = LinkMovementMethod()
//        }
//    }
//}