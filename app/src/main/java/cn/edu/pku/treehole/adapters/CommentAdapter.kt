package cn.edu.pku.treehole.adapters

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.edu.pku.treehole.adapters.bindingAdapter.HoleNumberLinkHelper
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.hole.CommentItemBean
import cn.edu.pku.treehole.databinding.CommentItemViewBinding
import cn.edu.pku.treehole.utilities.golden_ratio_conjugate
import timber.log.Timber
import java.util.*

/**
 *
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */

//class HoleAdapter :
class CommentAdapter(
    private val clickListener: CommentItemListener,
    private var contentTextSize: Int = LocalRepository.localGlobalHoleContentCurrentTextSize) :
    ListAdapter<CommentItemBean, CommentAdapter.ViewHolder>(CommentDiffCallback()) {

    init {
        namesColorMap.clear()
        globalRandomH = 0.0
    }
    companion object {
        var namesColorMap: HashMap<String, ArrayList<Int>> = HashMap()
        var globalRandomH: Double = 0.0
    }

    class ViewHolder(val binding: CommentItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listItem: CommentItemBean, clickListener: CommentItemListener, contentTextSize:Int) {
//            binding.apply{
//                holeListItemBean = listItem
//                executePendingBindings()
//            }
            binding.commentItemBean = listItem
            binding.holeContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, contentTextSize.toFloat());
            binding.clickListener = clickListener
            val selectColor = getColor(listItem, namesColorMap)?.get(0) ?: Color.WHITE
            binding.commentCsl.setBackgroundColor(selectColor)
            if(listItem.text?.isNotEmpty() == true) {
                val commentText = listItem.text
                val spannableString = SpannableString(listItem.text)
                if (commentText?.contains("Re") == true) {
                    val firstIndex = commentText.indexOf("Re")
                    val endIndex = if(commentText.indexOf(":") > 0) {
                        commentText.indexOf(":")
                    } else {
                        commentText.indexOf("：")
                    }
                    if(firstIndex + 3 < endIndex){
                        val reName = commentText.substring(firstIndex + 3, endIndex).lowercase(Locale.getDefault())
                        if(namesColorMap.containsKey(reName)){
                            spannableString.setSpan(
                                BackgroundColorSpan(
                                    namesColorMap[reName]?.get(0) ?: Color.WHITE
                                ), firstIndex + 3, endIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                            )
                        }

                    }
                }
                commentText?.let { HoleNumberLinkHelper.regexHoleText(it) }
                    ?.forEach { (value, indexRange) ->
                        val pid = value.substring(0)
                        Timber.e("$pid  $indexRange")
                        spannableString.setSpan(
                            HoleNumberLinkHelper.HoleTextClickSpan(pid),
                            indexRange.first,
                            indexRange.last + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                binding.holeContent.text = spannableString
            }

            binding.executePendingBindings()
        }

        private fun getColor(commentItemBean: CommentItemBean, namesColorMap:HashMap<String, ArrayList<Int>>): ArrayList<Int>? {
            if(globalRandomH.equals(0.0)){
                globalRandomH = if (commentItemBean.randomH.isNaN() || commentItemBean.randomH.equals(0.0)) {
                    Math.random()
                } else {
                    commentItemBean.randomH
                }
            }
            val name = commentItemBean.name.lowercase(Locale.getDefault())
            if(name == "洞主") {
                return arrayListOf(
                    ColorUtils.HSLToColor(floatArrayOf(0.0f, 0.0f, 0.97f)),
                    ColorUtils.HSLToColor(floatArrayOf(0.0f, 0.0f, 0.16f))
                )
            }
            if(!namesColorMap.containsKey(name)){
                globalRandomH += golden_ratio_conjugate
                globalRandomH %= 1
                namesColorMap[name] = arrayListOf(
                    ColorUtils.HSLToColor(floatArrayOf((globalRandomH*360).toFloat(), 0.5f, 0.9f)),
                    ColorUtils.HSLToColor(floatArrayOf((globalRandomH*360).toFloat(), 0.6f, 0.2f))
                )
            }
            return namesColorMap[name]
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CommentItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, contentTextSize)
//        holder.bind(getItem(position))
    }
}

class CommentDiffCallback : DiffUtil.ItemCallback<CommentItemBean>() {
    override fun areItemsTheSame(
        oldListItem: CommentItemBean,
        newListItem: CommentItemBean
    ): Boolean {
        return oldListItem.pid == newListItem.pid
    }

    override fun areContentsTheSame(
        oldListItem: CommentItemBean,
        newListItem: CommentItemBean
    ): Boolean {
        return oldListItem == newListItem
    }

}

class CommentItemListener(val clickListener: (item: CommentItemBean) -> Unit) {
    fun onClick(commentItemBean: CommentItemBean) = clickListener(commentItemBean)
}