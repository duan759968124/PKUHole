package cn.edu.pku.treehole.adapters

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.hole.CommentItemBean
import cn.edu.pku.treehole.data.hole.HoleInfoBean
import cn.edu.pku.treehole.data.hole.HoleItemBean
import cn.edu.pku.treehole.databinding.HoleItemView2Binding
import cn.edu.pku.treehole.databinding.HoleItemViewBinding
import cn.edu.pku.treehole.viewmodels.hole.HoleListViewModel
import cn.edu.pku.treehole.viewmodels.hole.PictureClickListener
import timber.log.Timber


/**
 *
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */

class HoleAdapter2(
    private val viewModel: HoleListViewModel,
    private var contentTextSize: Int = LocalRepository.localGlobalHoleContentCurrentTextSize
) :
    ListAdapter<HoleItemBean, HoleAdapter2.ViewHolder>(HoleDiffCallback2()) {
    class ViewHolder(val binding: HoleItemView2Binding) : RecyclerView.ViewHolder(binding.root) {
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
            viewModel: HoleListViewModel,
            contentTextSize:Int
        ) {
//            binding.apply{
//                holeListItemBean = listItem
//                executePendingBindings()
//            }
            binding.holeItemBean = listItem
            binding.viewModel = viewModel
            viewModel.getCommentList(listItem.pid)
//            when(listItem.reply){
//                0 ->{
//                    binding.commentBean1 = emptyCommentBean
//                    binding.commentBean2 = emptyCommentBean
//                }
//                1->{
//                    Timber.e("commment info 1: ${viewModel.result.value?.get(0)}")
//                    binding.commentBean1 = viewModel.result.value?.get(0) ?: emptyCommentBean
//                    binding.commentBean2 = emptyCommentBean
//                }
//                2->{
//                    Timber.e("commment info 2: ${viewModel.result.value?.get(0)}")
//                    binding.commentBean1 = viewModel.result.value?.get(0) ?: emptyCommentBean
//                    binding.commentBean2 = viewModel.result.value?.get(1) ?: emptyCommentBean
//                }
//            }
            binding.holeContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, contentTextSize.toFloat());
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HoleItemView2Binding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(getItem(position), itemClickListener, pictureClickListener, contentTextSize)
        holder.bind(getItem(position), viewModel, contentTextSize)
    }

}

class HoleDiffCallback2 : DiffUtil.ItemCallback<HoleItemBean>() {
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

//class HoleItemListener(val clickListener: (pid: Long) -> Unit) {
//    fun onClick(holeItem: HoleItemBean) = clickListener(holeItem.pid)
//}