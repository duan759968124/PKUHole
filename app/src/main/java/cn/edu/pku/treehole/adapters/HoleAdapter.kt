package cn.edu.pku.treehole.adapters

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.hole.HoleItemBean
import cn.edu.pku.treehole.databinding.HoleItemViewBinding
import cn.edu.pku.treehole.viewmodels.hole.PictureClickListener


/**
 *
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */

class HoleAdapter(
    private val itemClickListener: HoleItemListener,
    private val pictureClickListener: PictureClickListener,
    private var contentTextSize: Int = LocalRepository.localGlobalHoleContentCurrentTextSize
) :
    ListAdapter<HoleItemBean, HoleAdapter.ViewHolder>(HoleDiffCallback()) {
    class ViewHolder(val binding: HoleItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
//      暂时没用到
//        init {
//            binding.setClickListener {
//                binding.holeListItemBean?.let { holeItem ->
//                    navigateToHoleItemDetail(holeItem, it)
//                }
//            }
//        }
//        private fun navigateToHoleItemDetail(HoleItemBean: HoleItemBean, view: View) {
//            val direction = HoleViewPagerFragmentDirections.actionNavHoleToNavHoleDetail(HoleItemBean.pid)
//            view.findNavController().navigate(direction)
//        }


        fun bind(
            listItem: HoleItemBean,
            itemClickListener: HoleItemListener,
            pictureClickListener: PictureClickListener,
            contentTextSize:Int
        ) {
//            binding.apply{
//                holeListItemBean = listItem
//                executePendingBindings()
//            }
            binding.holeItemBean = listItem
            binding.holeContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, contentTextSize.toFloat());
            binding.clickListener = itemClickListener
            binding.pictureClickListener = pictureClickListener
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
        holder.bind(getItem(position), itemClickListener, pictureClickListener, contentTextSize)
//        holder.bind(getItem(position))
    }

//    暂时没必要
    fun setTextSizes(textSize: Int) {
        contentTextSize = textSize
        notifyDataSetChanged()
    }


}

class HoleDiffCallback : DiffUtil.ItemCallback<HoleItemBean>() {
    override fun areItemsTheSame(
        oldListItem: HoleItemBean,
        newListItem: HoleItemBean,
    ): Boolean {
        return oldListItem.pid == newListItem.pid
    }

    override fun areContentsTheSame(
        oldListItem: HoleItemBean,
        newListItem: HoleItemBean,
    ): Boolean {
        return oldListItem == newListItem
    }

}

class HoleItemListener(val clickListener: (pid: Long) -> Unit) {
    fun onClick(holeItem: HoleItemBean) = clickListener(holeItem.pid)
}