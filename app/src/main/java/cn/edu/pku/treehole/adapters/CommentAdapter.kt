package cn.edu.pku.treehole.adapters

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
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
//            if(listItem.text?.contains("Re") == true){
//                Timber.e("String: ${listItem.text}")
//                val spannableString = SpannableString(listItem.text)
//                val firstIndex = spannableString.indexOf("Re")
//                Timber.e("firstIndex: $firstIndex")
//                spannableString.setSpan(BackgroundColorSpan(Color.parseColor("#ff3c2a")), firstIndex, firstIndex + listItem.name.length + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
//                binding.holeContent.text = spannableString
//            }
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
                    Color.HSVToColor(floatArrayOf(0.0f, 0.0f, 97.0f / 100)),
                    Color.HSVToColor(floatArrayOf(0.0f, 0.0f, 16.0f / 100))
                )
            }
            if(!namesColorMap.containsKey(name)){
                globalRandomH += golden_ratio_conjugate
                globalRandomH %= 1
                namesColorMap[name] = arrayListOf(
                    Color.HSVToColor(floatArrayOf((globalRandomH*360).toFloat(), 0.5f, 90.0f / 100)),
                    Color.HSVToColor(floatArrayOf((globalRandomH*360).toFloat(), 0.6f, 20.0f / 100))
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