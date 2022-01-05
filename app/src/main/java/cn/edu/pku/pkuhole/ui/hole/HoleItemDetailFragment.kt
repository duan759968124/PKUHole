package cn.edu.pku.pkuhole.ui.hole

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import cn.edu.pku.pkuhole.R
import cn.edu.pku.pkuhole.adapters.CommentAdapter
import cn.edu.pku.pkuhole.adapters.CommentItemListener
import cn.edu.pku.pkuhole.base.BaseFragment
import cn.edu.pku.pkuhole.databinding.FragmentHoleItemDetailBinding
import cn.edu.pku.pkuhole.viewmodels.hole.HoleItemDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input


/**
 * A simple [Fragment] subclass.
 * Use the [HoleItemDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HoleItemDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentHoleItemDetailBinding
    private val viewModel : HoleItemDetailViewModel by viewModels()
    private lateinit var adapter : CommentAdapter

    private var isAttention : Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHoleItemDetailBinding.inflate(inflater, container, false)
        context ?: return binding.root
//        val arguments = HoleItemDetailFragmentArgs.fromBundle(requireArguments())
//        val dataSource =
//            AppDatabase.getInstance(requireNotNull(this.activity).application).holeAllListDao()
//        val viewModelFactory = HoleItemDetailViewModelFactory(arguments.pid, dataSource)
//        val holeItemDetailViewModel =
//            ViewModelProvider(this, viewModelFactory)[HoleItemDetailViewModel::class.java]
//        binding.holeItemDetailViewModel = holeItemDetailViewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        adapter = CommentAdapter(CommentItemListener { commentItem -> viewModel.onCommentItemClicked(commentItem) })
        binding.fragmentCommentListRecycler.adapter = adapter
        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        binding.fragmentCommentListRecycler.layoutManager = manager

        // 设置导航状态监听是否有必要
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun initObserve() {
        // 监听holeList变化并更新UI
        viewModel.commentList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // 监听currentHoleItem变化
        viewModel.currentHoleItem.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it.isAttention != null){
                    isAttention = it.isAttention!!
                    requireActivity().invalidateOptionsMenu()
                }
            }
        })

        // 监听加载更多状态
        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer {
            if(it){
                showLoading()
            }else{
                dismissLoading()
            }
        })

        // Todo：监听错误状态，好像有点问题
        viewModel.errorStatus.observe(viewLifecycleOwner, Observer {
//            Timber.e(it.toString())
            it.message?.let { it -> showToast(it) }
        })

        // 监听是否显示对话框
        viewModel.replyDialogToName.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                showReplyDialog("")
            } else {
                val prefillStr = "Re $it: "
                showReplyDialog(prefillStr)
            }
//            viewModel.onCommentDialogFinished()
        })

//         监听关注请求返回结果
        viewModel.responseMsg.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                showToast(it)
            }
        })
    }

    override fun initData() {
        viewModel.fetchCommentDetailFromNet()
    }

    @SuppressLint("CheckResult")
    fun showReplyDialog(paramString: String?) {
        context?.let {
            MaterialDialog(it).show {
                title(R.string.reply)
                input(hintRes = R.string.hint_reply_text, prefill = paramString) { dialog, text ->
//                    Timber.e("input text %s", text)
                    // 检查文本并发送回复文本请求
                    checkReplyAndRequest(text)
//                    if(!paramString.isNullOrEmpty()){
//                        Timber.e("input not null param text --%s--", text.substring(text.indexOf(":")+1).trim().isEmpty())
//                    }else{
//                        Timber.e("input text %s", text)
//                    }
//                    val inputField = dialog.getInputField()
//
//                    val isValid = !(!paramString.isNullOrEmpty() and text.substring(text.indexOf(":")+1).trim().isEmpty())
//                    Timber.e("isValid %s", isValid)
//                    inputField?.error = if (isValid) null else "Must !"
//                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
                }
                negativeButton(R.string.cancelReply)
//                { dialog ->
//                    dialog.hide()
//                }
                positiveButton(R.string.reply)
            }
        }
    }

    private fun checkReplyAndRequest(text: CharSequence) {
        if(text.isEmpty() or (text.indexOf("Re") != -1 && text.substring(text.indexOf(":")+1).trim().isEmpty())){
            showToast("回复不可为空")
        } else{
            viewModel.sendReplyComment(text)
        }
    }


    @SuppressLint("CheckResult")
    fun showReportDialog() {
        context?.let {
            MaterialDialog(it).show {
                title(R.string.report_title)
                input(hintRes = R.string.hint_report_text) { dialog, text ->
//                    Timber.e("input text %s", text)
                    // 检查文本并发送回复文本请求
                    checkReportAndRequest(text)
//                    if(!paramString.isNullOrEmpty()){
//                        Timber.e("input not null param text --%s--", text.substring(text.indexOf(":")+1).trim().isEmpty())
//                    }else{
//                        Timber.e("input text %s", text)
//                    }
//                    val inputField = dialog.getInputField()
//
//                    val isValid = !(!paramString.isNullOrEmpty() and text.substring(text.indexOf(":")+1).trim().isEmpty())
//                    Timber.e("isValid %s", isValid)
//                    inputField?.error = if (isValid) null else "Must !"
//                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
                }
                negativeButton(R.string.cancelReply)
//                { dialog ->
//                    dialog.hide()
//                }
                positiveButton(R.string.reply)
            }
        }
    }

    private fun checkReportAndRequest(text: CharSequence) {
        if(text.isEmpty()){
            showToast("举报原因不可为空")
        } else{
            viewModel.sendReportReason(text)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_hole_detail, menu)
    }



    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if(isAttention == 1){
            menu.findItem(R.id.button_attention).setIcon(R.drawable.ic_star_fill_24)
        } else{
            menu.findItem(R.id.button_attention).setIcon(R.drawable.ic_star_24)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val navController = findNavController()
//        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.button_attention -> {
                // 多次点击要拿到状态是否已经关注过了，
                // 如果关注过，就取消关注，否则就关注
                // 先发请求，之后根据请求状态再来处理图标变化
                viewModel.switchAttentionStatus()
//                if(isAttention == 1){
//                    // 取消关注
//                    item.setIcon(R.drawable.ic_star_24)
//                }
//                item.setIcon(R.drawable.ic_star_fill_24)
                // Todo: 返回true和返回false之前的区别【暂时没搞懂】
                false
            }
            R.id.button_report -> {
                // Todo: 举报功能没有测试
                // 弹出对话框发网络请求到服务器投诉举报，并处理结果。
                showReportDialog()
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}