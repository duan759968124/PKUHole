package cn.edu.pku.treehole.ui.hole

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import cn.edu.pku.treehole.NavigationDirections
import cn.edu.pku.treehole.R
import cn.edu.pku.treehole.adapters.HoleAdapter
import cn.edu.pku.treehole.adapters.HoleItemListener
import cn.edu.pku.treehole.base.BaseFragment
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.databinding.FragmentHoleListBinding

import cn.edu.pku.treehole.viewmodels.hole.HoleListViewModel
import cn.edu.pku.treehole.viewmodels.hole.PictureClickListener
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.checkbox.checkBoxPrompt
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import kotlin.concurrent.schedule

@AndroidEntryPoint
class HoleListFragment : BaseFragment() {

    private lateinit var binding: FragmentHoleListBinding
    private val viewModel: HoleListViewModel by viewModels()
    private lateinit var adapter : HoleAdapter

    @SuppressLint("TimberArgCount")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHoleListBinding.inflate(inflater, container, false)
        context ?: return binding.root
//        val adapter = HoleAdapter()
        adapter = HoleAdapter(
            HoleItemListener { pid -> viewModel.onHoleItemClicked(pid) },
            PictureClickListener { holeItem -> previewPicture(holeItem) })
        binding.fragmentHoleListRecycler.adapter = adapter
        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        binding.fragmentHoleListRecycler.layoutManager = manager

//        binding.fragmentHoleListSrl.setEnableScrollContentWhenRefreshed(true);
        binding.fragmentHoleListSrl.setDisableContentWhenRefresh(false)
        binding.fragmentHoleListSrl.setDisableContentWhenLoading(false)
//        binding.fragmentHoleListSrl.setEnableHeaderTranslationContent(true)
//        binding.fragmentHoleListSrl.setEnableNestedScroll(true);
//        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun initObserve() {
        // 刷新监听
        binding.fragmentHoleListSrl.setOnRefreshListener {
            viewModel.refreshHoleList()
            Timber.e("监听到下拉刷新了")
//            it.finishRefresh()
        }
        // 加载更多监听
        binding.fragmentHoleListSrl.setOnLoadMoreListener {
            viewModel.getHoleList()
            Timber.e("监听到加载更多了")
//            it.finishLoadMore(false)
        }

        viewModel.getRandomTipFromNet.observe(viewLifecycleOwner) {
            if (it) {
                showRandomTipDialog()
                viewModel.closeRandomTipDialog()
            }
        }

        // 监听holeList变化并更新UI
        viewModel.holeList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // 监听刷新状态变化
        viewModel.refreshStatus.observe(viewLifecycleOwner) {
            Timber.e("refreshStatus=$it")
            if (it) {
                Timber.e("show refresh")
//                binding.fragmentHoleListSrl.autoRefreshAnimationOnly();//自动刷新，只显示动画不执行刷新
//                showLoading()
            } else {
                Timber.e("hide refresh")
//                dismissLoading()
                binding.fragmentHoleListSrl.finishRefresh(500)
                Timer().schedule(1500) {
                    binding.fragmentHoleListRecycler.smoothScrollToPosition(0)
                }

            }
        }

        // 监听加载更多状态
        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer {
            if(it){
                Timber.e("show loading")
//                binding.fragmentHoleListSrl.autoRefreshAnimationOnly();//自动刷新，只显示动画不执行刷新
//                showLoading()
            }else{
                Timber.e("hide loading")
//                dismissLoading()
                binding.fragmentHoleListSrl.finishLoadMore(1000)
            }
        })

        viewModel.navigationToHoleItemDetail.observe(viewLifecycleOwner, Observer { pid ->
            pid?.let {
//                findNavController()
//                    .navigate(HoleViewPagerFragmentDirections.actionNavHoleToNavHoleDetail(pid))
                findNavController().navigate(NavigationDirections.actionGlobalNavHoleDetail(pid))
                viewModel.onHoleItemDetailNavigated()
            }
        })


        // 系统网络报错
        viewModel.errorStatus.observe(viewLifecycleOwner, Observer { error ->
            error.message?.let { showToast("错误-$it") }
        })

        // api报错
        viewModel.failStatus.observe(viewLifecycleOwner, Observer { fail ->
            fail.message?.let { showToast("失败-$it") }
        })
        // 退出到login界面
        viewModel.loginStatus.observe(viewLifecycleOwner, Observer { isLogin ->
            Timber.e("isLogin %s", isLogin)
            if (!isLogin) {
                // 全局导航操作
                findNavController().navigate(R.id.action_global_nav_login)
                viewModel.onNavigateToLoginFinish()
            }
        })

    }

    private fun showRandomTipDialog() {
        if (LocalRepository.checkIsShowTip()) {
            context?.let {
                MaterialDialog(it).show {
                    title(R.string.hole_practice)
                    cancelable(false)
                    cancelOnTouchOutside(false)
                    message(text = LocalRepository.localRandomTip)
                    checkBoxPrompt(R.string.hiddenDialogToday) { checked ->
                        LocalRepository.hiddenRandomTipToday = checked
                    }
                    positiveButton(R.string.confirm)
                }
            }
        }
    }

    override fun initData() {
//        viewModel.getHoleList()
//         监听数据的状态变化
//        viewModel.mHoleListLiveData.observe(this,
//            object : IStateObserver<List<HoleListItemBean>>(binding?.holeListRecycler) {
//                override fun onDataChange(data: List<HoleListItemBean>?) {
//                    super.onDataChange(data)
//                    Timber.d("onDataChange: ")
//                    data?.let { viewModel.insertToDatabase(it)  }
////                    data?.let { adapter.setData(it) }
//                }
//
//                override fun onReload(v: View?) {
//                    Timber.d("onReload: ")
////                    mViewModel.loadProjectTree()
//                }
//
//                override fun onDataEmpty() {
//                    super.onDataEmpty()
//                    Timber.d("onDataEmpty: ")
//                }
//
//                override fun onError(e: Throwable?) {
//                    super.onError(e)
//                    // 最终显示Error Toast是在最后，分情况讨论，系统的Error，在此之前就Toast好，不用处理，API的Faile（error）在这处理
//                    showToast(e?.message!!)
//                    Timber.d("onError: ${e?.printStackTrace()}")
//                }
//            })
    }



}