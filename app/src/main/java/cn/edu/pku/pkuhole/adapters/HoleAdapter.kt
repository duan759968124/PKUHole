package cn.edu.pku.pkuhole.adapters

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.edu.pku.pkuhole.data.hole.HoleItemBean
import cn.edu.pku.pkuhole.databinding.HoleItemViewBinding
import cn.edu.pku.pkuhole.viewmodels.hole.PictureClickListener
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import cn.edu.pku.pkuhole.adapters.bindingAdapter.HoleTextClickSpan
import timber.log.Timber


/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */

class HoleAdapter(private val clickListener: HoleItemListener, private val pictureClickListener: PictureClickListener) :
    ListAdapter<HoleItemBean, HoleAdapter.ViewHolder>(HoleDiffCallback()) {
    class ViewHolder(val binding: HoleItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
//        init {
//            binding.setClickListener {
//                binding.holeListItemBean?.let { holeItem ->
//                    navigateToHoleItemDetail(holeItem, it)
//                }
//            }
//        }
//      暂时没用到
        init{
            binding.setLongClickListener {
                Timber.e("long click textview")
                true
            }
        }
//        init {
//            binding.holeNumberClickListener = HoleTextClickSpan.
//        }

//        private fun navigateToHoleItemDetail(HoleItemBean: HoleItemBean, view: View) {
//            val direction = HoleViewPagerFragmentDirections.actionNavHoleToNavHoleDetail(HoleItemBean.pid)
//            view.findNavController().navigate(direction)
//        }

        fun bind(listItem: HoleItemBean, clickListener: HoleItemListener, pictureClickListener: PictureClickListener) {
//            binding.apply{
//                holeListItemBean = listItem
//                executePendingBindings()
//            }
            binding.holeItemBean = listItem
            binding.clickListener = clickListener
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
        holder.bind(getItem(position), clickListener, pictureClickListener)
//        holder.bind(getItem(position))
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