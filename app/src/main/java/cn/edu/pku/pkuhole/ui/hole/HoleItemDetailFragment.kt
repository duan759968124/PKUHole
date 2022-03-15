package cn.edu.pku.pkuhole.ui.hole

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import cn.edu.pku.pkuhole.R
import cn.edu.pku.pkuhole.adapters.CommentAdapter
import cn.edu.pku.pkuhole.adapters.CommentItemListener
import cn.edu.pku.pkuhole.base.BaseFragment
import cn.edu.pku.pkuhole.databinding.FragmentHoleItemDetailBinding
import cn.edu.pku.pkuhole.utilities.GlideEngine
import cn.edu.pku.pkuhole.viewmodels.hole.HoleItemDetailViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class HoleItemDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentHoleItemDetailBinding
    private val viewModel: HoleItemDetailViewModel by viewModels()
    private lateinit var adapter: CommentAdapter

    private var isAttention: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
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
        viewModel.commentList.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        // 监听currentHoleItem变化
        viewModel.currentHoleItem.observe(viewLifecycleOwner, {
            it?.let {
                if (it.isAttention != null) {
                    isAttention = it.isAttention!!
                    requireActivity().invalidateOptionsMenu()
                }
            }
        })

        viewModel.loadingStatus.observe(viewLifecycleOwner, {
            if (it) {
                showLoading()
            } else {
                dismissLoading()
            }
        })

        // 监听是否显示对话框
        viewModel.replyDialogToName.observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                showReplyDialog("")
            } else {
                val prefillStr = "Re $it: "
                showReplyDialog(prefillStr)
            }
//            viewModel.onCommentDialogFinished()
        })
        // 监听是否预览图片
        viewModel.previewPicture.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()) {
                val localMedia = LocalMedia()
                localMedia.path = it
                localMedia.setPosition(0)
                val selectList: ArrayList<LocalMedia> = ArrayList(1)
                selectList.add(localMedia)
                PictureSelector.create(this)
                    .themeStyle(R.style.picture_WeChat_style)
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .isNotPreviewDownload(true)
                    .imageEngine(GlideEngine.createGlideEngine())
                    .openExternalPreview(0, selectList)
                viewModel.finishPreviewPic()
            }
        })

//         监听关注请求返回结果
        viewModel.responseMsg.observe(viewLifecycleOwner, {
            if (it != null) {
                showToast(it)
            }
        })

        // 系统网络报错
        viewModel.errorStatus.observe(viewLifecycleOwner, { error ->
            error.message?.let { showToast("错误-$it") }
        })

        // api报错
        viewModel.failStatus.observe(viewLifecycleOwner, { fail ->
            fail.message?.let { showToast("失败-$it") }
        })
        // 退出到login界面
        viewModel.loginStatus.observe(viewLifecycleOwner, { isLogin ->
            Timber.e("isLogin %s", isLogin)
            if (!isLogin) {
                // 全局导航操作
                findNavController().navigate(R.id.action_global_nav_login)
                viewModel.onNavigateToLoginFinish()
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
                positiveButton(R.string.report)
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
        inflater.inflate(R.menu.menu_hole_detail_toolbar, menu)
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
                // 弹出对话框发网络请求到服务器投诉举报，并处理结果。
                showReportDialog()
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}