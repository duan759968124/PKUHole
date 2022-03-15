//package cn.edu.pku.pkuhole.adapters.bindingAdapter
//
//import android.annotation.SuppressLint
//import android.text.Spannable
//import android.text.SpannableString
//import android.text.Spanned
//import android.text.TextPaint
//import android.text.method.LinkMovementMethod
//import android.text.style.ClickableSpan
//import android.view.View
//import android.widget.TextView
//import androidx.databinding.BindingAdapter
//import androidx.navigation.findNavController
//import cn.edu.pku.pkuhole.NavigationDirections
//import cn.edu.pku.pkuhole.R
//import cn.edu.pku.pkuhole.data.hole.HoleItemBean
//import timber.log.Timber
//
///**
// *
// * @Author:         HuidongQin
// * @e-mail:         hdqin@pku.edu.cn
// * @Time:           2022/3/9
// * @Desc:
// * @Version:        1.0
// */
//
//object HoleNumberLinkHelper {
//    class HoleTextClickSpan(var pid: String) : ClickableSpan() {
//
//        var listener: OnClickListener? = null
//        interface OnClickListener {
//            fun onSpanClick(widget: View, pid: String)
//        }
//
//        override fun onClick(widget: View) {
//            Timber.e("click Hole Number: $pid")
//            listener?.onSpanClick(widget, pid)
//            //        private fun navigateToHoleItemDetail(HoleItemBean: HoleItemBean, view: View) {
////            widget.findNavController().navigate(NavigationDirections.actionGlobalNavHoleDetail(pid.toLong()))
////        }
//        }
//
//        @SuppressLint("ResourceAsColor")
//        override fun updateDrawState(ds: TextPaint) {
//            super.updateDrawState(ds)
//            ds.color = R.color.hole_blue
//            ds.isUnderlineText = true
//        }
//    }
//
//    // 正则hole text内容
//    fun regexHoleText(text: String): HashMap<String, IntRange> {
//        //设置正则获取多个树洞号
//        val holeNumberMap = HashMap<String, IntRange>()
//        val holeNumberPattern = "#[0-9]+".toRegex()
//        val found = holeNumberPattern.findAll(text)
//        found.forEach { f ->
//            val m = f.value
//            val idx = f.range
//            holeNumberMap[m] = idx
//        }
//        return holeNumberMap
//    }
//
//    /**
//     * 设置点击事件。
//     */
//    fun setClickListener(spanned: Spanned, listener: HoleTextClickSpan.OnClickListener) {
//        spanned.getSpans(0, spanned.length, HoleTextClickSpan::class.java)
//            .forEach { it.listener = listener }
//    }
//}
//
//class HoleTextNumberListener(val clickListener: (pid: Long) -> Unit) {
//    fun onClick(holeItem: HoleItemBean) = clickListener(holeItem.pid)
//}
//
//
//
//
//@BindingAdapter(value = ["binding_hole_text", "binding_hole_number_listener"], requireAll = true)
//fun TextView.setHandleHoleText(holeText: String?, listener: HoleNumberLinkHelper.HoleTextClickSpan.OnClickListener?){
//    if (holeText != null) {
//        if(holeText.isNotEmpty()){
//            val spannableString = SpannableString(holeText)
//            //正则查找所有的树洞号，并标记下来，设置为可点击
//            val holeNumMap = HoleNumberLinkHelper.regexHoleText(holeText)
//            holeNumMap.forEach { (value, indexRange) ->
//                val pid = value.substring(1)
//                Timber.e("$pid  $indexRange")
//                spannableString.setSpan(HoleNumberLinkHelper.HoleTextClickSpan(pid), indexRange.first, indexRange.last + 1,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//            }
//            spannableString.apply {
//                if (listener != null) {
//                    HoleNumberLinkHelper.setClickListener(this, listener)
//                }
//            }
//            text = spannableString
//            movementMethod = LinkMovementMethod()
//
//        }
//    }
//}
//
