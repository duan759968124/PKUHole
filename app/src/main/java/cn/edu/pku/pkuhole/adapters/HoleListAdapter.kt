package cn.edu.pku.pkuhole.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.edu.pku.pkuhole.data.hole.HoleListItemBean
import cn.edu.pku.pkuhole.databinding.HoleItemViewBinding
import cn.edu.pku.pkuhole.ui.hole.HoleViewPagerFragmentDirections

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */

// class HoleAllListAdapter(private val clickListener: HoleItemListener) :
class HoleListAdapter :
    ListAdapter<HoleListItemBean, HoleListAdapter.ViewHolder>(HoleAllListDiffCallback()) {
    class ViewHolder(val binding: HoleItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
//  fun bind(item: HoleAllListItemBean, clickListener: HoleItemListener) {
        init {
            binding.setClickListener {
                binding.holeListItemBean?.let { holeItem ->
                    navigateToHoleItemDetail(holeItem, it)
                }
            }
        }

        private fun navigateToHoleItemDetail(holeListItem: HoleListItemBean, view: View) {
            val direction = HoleViewPagerFragmentDirections.actionNavHoleToNavHoleDetail(holeListItem.pid)
            view.findNavController().navigate(direction)
        }

        fun bind(listItem: HoleListItemBean) {
            binding.apply{
                holeListItemBean = listItem
                executePendingBindings()
            }
//            binding.holeAllListItemBean = item
//            binding.clickListener = clickListener
//            binding.executePendingBindings()
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
//        holder.bind(getItem(position), clickListener)
        holder.bind(getItem(position))
    }
}

class HoleAllListDiffCallback : DiffUtil.ItemCallback<HoleListItemBean>() {
    override fun areItemsTheSame(
        oldListItem: HoleListItemBean,
        newListItem: HoleListItemBean
    ): Boolean {
        return oldListItem.pid == newListItem.pid
    }

    override fun areContentsTheSame(
        oldListItem: HoleListItemBean,
        newListItem: HoleListItemBean
    ): Boolean {
        return oldListItem == newListItem
    }

}

//class HoleItemListener(val clickListener: (pid: Long) -> Unit) {
//    fun onClick(holeItem: HoleAllListItemBean) = clickListener(holeItem.pid)
//}