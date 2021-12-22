package cn.edu.pku.pkuhole.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.edu.pku.pkuhole.data.hole.HoleAllListItemBean
import cn.edu.pku.pkuhole.databinding.HoleItemViewBinding

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */
class HoleAllListAdapter :
    ListAdapter<HoleAllListItemBean, HoleAllListAdapter.ViewHolder>(HoleAllListDiffCallback()) {
    class ViewHolder(val binding: HoleItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HoleAllListItemBean) {
            binding.holeAllListItemBean = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HoleItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class HoleAllListDiffCallback : DiffUtil.ItemCallback<HoleAllListItemBean>() {
    override fun areItemsTheSame(
        oldItem: HoleAllListItemBean,
        newItem: HoleAllListItemBean
    ): Boolean {
        return oldItem.pid == newItem.pid
    }

    override fun areContentsTheSame(
        oldItem: HoleAllListItemBean,
        newItem: HoleAllListItemBean
    ): Boolean {
        return oldItem == newItem
    }

}