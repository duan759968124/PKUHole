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
import cn.edu.pku.treehole.data.hole.HoleInfoBean
import cn.edu.pku.treehole.databinding.HoleItemView3Binding
import cn.edu.pku.treehole.databinding.HoleItemViewBinding
import cn.edu.pku.treehole.utilities.golden_ratio_conjugate
import cn.edu.pku.treehole.viewmodels.hole.PictureClickListener
import cn.edu.pku.treehole.viewmodels.hole.PictureClickListener2
import timber.log.Timber
import java.util.*


/**
 *
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */

class HoleAdapter3(
    private val itemClickListener: HoleItemListener2,
    private val pictureClickListener: PictureClickListener2,
    private var contentTextSize: Int = LocalRepository.localGlobalHoleContentCurrentTextSize
) :
    ListAdapter<HoleInfoBean, HoleAdapter3.ViewHolder>(HoleDiffCallback3()) {

    init {
        namesColorMap.clear()
        globalRandomH = 0.0
    }

    companion object {
        var namesColorMap: HashMap<String, ArrayList<Int>> = HashMap()
        var globalRandomH: Double = 0.0
    }

    class ViewHolder(val binding: HoleItemView3Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            listItem: HoleInfoBean,
            itemClickListener2: HoleItemListener2,
            pictureClickListener2: PictureClickListener2,
            contentTextSize:Int
        ) {
            binding.holeInfoBean = listItem
            binding.holeContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, contentTextSize.toFloat());
            binding.clickListener = itemClickListener2
            binding.pictureClickListener = pictureClickListener2
            namesColorMap.clear()
            when(listItem.holeItemBean.reply) {
                1 -> {
                    globalRandomH = listItem.commentItemBean1?.randomH ?: 0.0
                    binding.commentCard1.commentCsl.setBackgroundColor(
                        listItem.commentItemBean1?.let { getColor(it) }?.get(0) ?: Color.WHITE)
                    if(listItem.commentItemBean1?.text?.isNotEmpty() == true) {
                        val spannableString =
                            spannableCommentTextString(listItem.commentItemBean1!!)
                        binding.commentCard1.commentContent.text = spannableString
                    }
                }
                2 -> {
                    globalRandomH = listItem.commentItemBean1?.randomH ?: 0.0
                    binding.commentCard1.commentCsl.setBackgroundColor(
                        listItem.commentItemBean1?.let { getColor(it) }?.get(0) ?: Color.WHITE)
                    binding.commentCard2.commentCsl.setBackgroundColor(
                        listItem.commentItemBean2?.let { getColor(it) }?.get(0) ?: Color.WHITE)

                    if(listItem.commentItemBean1?.text?.isNotEmpty() == true) {
                        val spannableString =
                            spannableCommentTextString(listItem.commentItemBean1!!)
                        binding.commentCard1.commentContent.text = spannableString
                    }
                    if(listItem.commentItemBean2?.text?.isNotEmpty() == true) {
                        val spannableString =
                            spannableCommentTextString(listItem.commentItemBean2!!)
                        binding.commentCard2.commentContent.text = spannableString
                    }
                }
            }
            binding.executePendingBindings()
        }

        private fun spannableCommentTextString(comment: CommentItemBean): SpannableString {
            val commentText = comment.text
            val spannableString = SpannableString(comment.text)
            if (commentText?.contains("Re") == true) {
                val firstIndex = commentText.indexOf("Re")
                val endIndex = if (commentText.indexOf(":") > 0) {
                    commentText.indexOf(":")
                } else {
                    commentText.indexOf("：")
                }
                if (firstIndex + 3 < endIndex) {
                    val reName = commentText.substring(firstIndex + 3, endIndex)
                        .lowercase(Locale.getDefault())
                    if (namesColorMap.containsKey(reName)) {
                        spannableString.setSpan(
                            BackgroundColorSpan(
                                CommentAdapter.namesColorMap[reName]?.get(0) ?: Color.WHITE
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
            return spannableString
        }

        private fun getColor(commentItemBean: CommentItemBean): ArrayList<Int>? {
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
                    ColorUtils.HSLToColor(floatArrayOf((globalRandomH *360).toFloat(), 0.5f, 0.9f)),
                    ColorUtils.HSLToColor(floatArrayOf((globalRandomH *360).toFloat(), 0.6f, 0.2f))
                )
            }
            Timber.e("nameColorMap: $namesColorMap")
            return namesColorMap[name]
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HoleItemView3Binding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), itemClickListener, pictureClickListener, contentTextSize)
//        holder.bind(getItem(position))
    }

}

class HoleDiffCallback3 : DiffUtil.ItemCallback<HoleInfoBean>() {
    override fun areItemsTheSame(
        oldListItem: HoleInfoBean,
        newListItem: HoleInfoBean,
    ): Boolean {
        return oldListItem.pid == newListItem.pid
    }

    override fun areContentsTheSame(
        oldListItem: HoleInfoBean,
        newListItem: HoleInfoBean,
    ): Boolean {
        return oldListItem == newListItem
    }

}

class HoleItemListener2(val clickListener: (pid: Long) -> Unit) {
    fun onClick(holeItem: HoleInfoBean) = clickListener(holeItem.pid)
}