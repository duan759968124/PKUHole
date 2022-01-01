package cn.edu.pku.pkuhole.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.edu.pku.pkuhole.data.hole.HoleItemModel
import cn.edu.pku.pkuhole.databinding.HoleItemViewBinding

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */

//class HoleAdapter :
class HoleAdapter(private val clickListener: HoleItemListener) :
    ListAdapter<HoleItemModel, HoleAdapter.ViewHolder>(HoleDiffCallback()) {
    class ViewHolder(val binding: HoleItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
//        init {
//            binding.setClickListener {
//                binding.holeListItemBean?.let { holeItem ->
//                    navigateToHoleItemDetail(holeItem, it)
//                }
//            }
//        }

//        private fun navigateToHoleItemDetail(holeItemModel: HoleItemModel, view: View) {
//            val direction = HoleViewPagerFragmentDirections.actionNavHoleToNavHoleDetail(holeItemModel.pid)
//            view.findNavController().navigate(direction)
//        }

        fun bind(listItem: HoleItemModel, clickListener: HoleItemListener) {
//            binding.apply{
//                holeListItemBean = listItem
//                executePendingBindings()
//            }
            binding.holeItemModel = listItem
            binding.clickListener = clickListener
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
        holder.bind(getItem(position), clickListener)
//        holder.bind(getItem(position))
    }
}

class HoleDiffCallback : DiffUtil.ItemCallback<HoleItemModel>() {
    override fun areItemsTheSame(
        oldListItem: HoleItemModel,
        newListItem: HoleItemModel
    ): Boolean {
        return oldListItem.pid == newListItem.pid
    }

    override fun areContentsTheSame(
        oldListItem: HoleItemModel,
        newListItem: HoleItemModel
    ): Boolean {
        return oldListItem == newListItem
    }

}

class HoleItemListener(val clickListener: (pid: Long) -> Unit) {
    fun onClick(holeItem: HoleItemModel) = clickListener(holeItem.pid)
}