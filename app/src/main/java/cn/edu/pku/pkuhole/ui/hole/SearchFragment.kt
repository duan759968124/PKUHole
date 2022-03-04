package cn.edu.pku.pkuhole.ui.hole

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import cn.edu.pku.pkuhole.R
import cn.edu.pku.pkuhole.adapters.HoleAdapter
import cn.edu.pku.pkuhole.adapters.HoleItemListener
import cn.edu.pku.pkuhole.base.BaseFragment
import cn.edu.pku.pkuhole.databinding.FragmentAttentionBinding
import cn.edu.pku.pkuhole.databinding.FragmentHoleListBinding
import cn.edu.pku.pkuhole.databinding.FragmentSearchBinding
import cn.edu.pku.pkuhole.viewmodels.hole.AttentionViewModel

import cn.edu.pku.pkuhole.viewmodels.hole.HoleListViewModel
import cn.edu.pku.pkuhole.viewmodels.hole.PictureClickListener
import cn.edu.pku.pkuhole.viewmodels.hole.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: HoleAdapter

    @SuppressLint("TimberArgCount")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        context ?: return binding.root
        adapter = HoleAdapter(
            HoleItemListener { pid -> viewModel.onHoleItemClicked(pid) },
            PictureClickListener { url -> previewPicture(url) }
        )
        binding.fragmentSearchListRecycler.adapter = adapter
        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        binding.fragmentSearchListRecycler.layoutManager = manager

//        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun initObserve() {


        // 监听holeList变化并更新UI
        viewModel.searchList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigationToHoleItemDetail.observe(viewLifecycleOwner, Observer { pid ->
            pid?.let {
                findNavController()
                    .navigate(SearchFragmentDirections.actionNavSearchResultToNavHoleDetail(pid))
                viewModel.onHoleItemDetailNavigated()
            }
        })

        // 正在加载数据
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

    override fun initData() {
//        viewModel.getSearchList()
    }



}