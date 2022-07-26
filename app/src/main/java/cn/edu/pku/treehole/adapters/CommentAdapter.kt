package cn.edu.pku.treehole.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.edu.pku.treehole.data.hole.CommentItemBean
import cn.edu.pku.treehole.databinding.CommentItemViewBinding

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */

//class HoleAdapter :
class CommentAdapter(private val clickListener: CommentItemListener) :
    ListAdapter<CommentItemBean, CommentAdapter.ViewHolder>(CommentDiffCallback()) {
    class ViewHolder(val binding: CommentItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listItem: CommentItemBean, clickListener: CommentItemListener) {
//            binding.apply{
//                holeListItemBean = listItem
//                executePendingBindings()
//            }
            binding.commentItemBean = listItem
            binding.clickListener = clickListener
            binding.executePendingBindings()
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
        holder.bind(getItem(position), clickListener)
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