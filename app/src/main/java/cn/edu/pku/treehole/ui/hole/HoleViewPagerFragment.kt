package cn.edu.pku.treehole.ui.hole

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import cn.edu.pku.treehole.R
import cn.edu.pku.treehole.adapters.HOLE_LIST_INDEX
import cn.edu.pku.treehole.adapters.HOLE_MY_ATTENTION_INDEX
import cn.edu.pku.treehole.adapters.HolePaperAdapter
import cn.edu.pku.treehole.base.BaseFragment
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.databinding.FragmentHoleViewPagerBinding
import cn.edu.pku.treehole.viewmodels.hole.HoleViewPagerViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.checkbox.checkBoxPrompt
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 *
 * @Time:           2021/12/21
 * @Desc:
 * @Version:        1.0
 */
@AndroidEntryPoint
class HoleViewPagerFragment : BaseFragment() {

    private lateinit var binding: FragmentHoleViewPagerBinding
    private val viewModel : HoleViewPagerViewModel by activityViewModels()

    private lateinit var searchDialog: MaterialDialog
    private lateinit var postDialog: PostHoleDialogFragment
    private lateinit var tagSheetDialog: MaterialDialog

    private var searchTagName: String = ""
    private val tagTitle: String = "选择标签"
    private var tagNameList = listOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoleViewPagerBinding.inflate(inflater, container, false)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        viewPager.adapter = HolePaperAdapter(this)
        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            tab.text = getTabList(position)
        }.attach()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        searchDialog = context?.let {
            MaterialDialog(it)
                .title(R.string.search)
                .input(hintRes = R.string.hint_search_text) { dialog, text ->
                    // 检查文本并发送回复文本请求
                    checkSearchAndRequest(text)
                }
                .negativeButton(R.string.cancelReply)
                .positiveButton(R.string.search)
                .neutralButton(text = tagTitle){
                    tagSheetDialog.show()
                }
        }!!

        postDialog = PostHoleDialogFragment()

        viewModel.showDialogPost.observe(viewLifecycleOwner, Observer { isShow ->
            if(isShow){
                Timber.e("show dialog")
                postDialog.show(parentFragmentManager, "dialog")
            }else{
                if(postDialog.isVisible){
                    postDialog.dismiss()
                }

            }
        })

        viewModel.tagNameList.observe(viewLifecycleOwner) {
            it?.let {
                tagNameList = it
                tagSheetDialog = context?.let { context ->
                    MaterialDialog(context)
                        .title(text = tagTitle)
                        .listItems(items = tagNameList){ dialog, index, text ->
                            searchTagName = text as String
                            searchDialog.neutralButton(text = searchTagName).show()
                        }
                }!!
            }
        }

        // 监听发布更新帖子
        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer {
            if(it){
                showLoading()
            }else{
                dismissLoading()
            }
        })

        // 系统网络报错
        viewModel.errorStatus.observe(viewLifecycleOwner, Observer { error ->
            error.message?.let { showToast("错误-$it") }
        })

        // api报错
        viewModel.failStatus.observe(viewLifecycleOwner) { fail ->
            fail.message?.let { showToast("失败-$it") }
        }
        // 退出到login界面
        viewModel.loginStatus.observe(viewLifecycleOwner) { isLogin ->
            Timber.e("isLogin %s", isLogin)
            if (!isLogin) {
                // 全局导航操作
                findNavController().navigate(R.id.action_global_nav_login)
                viewModel.onNavigateToLoginFinish()
            }
        }

        viewModel.postResponseMsg.observe(viewLifecycleOwner, Observer { msg ->
            if (msg != null) {
                showToast(msg)
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun initData() {

    }


    private fun getTabList(position: Int): String? {
        return when (position) {
            HOLE_LIST_INDEX -> getString(R.string.hole_list)
            HOLE_MY_ATTENTION_INDEX -> getString(R.string.my_attention)
            else -> null
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_hole_toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val navController = findNavController()
//        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        return when(item.itemId){
            R.id.button_search ->{
//                showToast("click search")
                searchDialog.show()
                false
            }
            R.id.button_edit -> {
                Timber.e("show dialog")
                postDialog.show(parentFragmentManager, "dialog")
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun checkSearchAndRequest(text: CharSequence) {
        if(text.isEmpty()){
            showToast("关键词不可为空")
        } else{
            //跳转到搜索界面
            findNavController().navigate(HoleViewPagerFragmentDirections.actionNavHoleToNavSearchResult(
                keywords = text.toString(),
                searchTagName = searchTagName))
        }
        searchTagName = ""
    }

}