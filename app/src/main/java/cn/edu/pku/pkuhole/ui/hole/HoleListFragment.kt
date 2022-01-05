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
import cn.edu.pku.pkuhole.adapters.HoleAdapter
import cn.edu.pku.pkuhole.adapters.HoleItemListener
import cn.edu.pku.pkuhole.base.BaseFragment
import cn.edu.pku.pkuhole.databinding.FragmentHoleListBinding

import cn.edu.pku.pkuhole.viewmodels.hole.HoleListViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
        adapter = HoleAdapter(HoleItemListener { pid -> viewModel.onHoleItemClicked(pid) })
        binding.fragmentHoleListRecycler.adapter = adapter
        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        binding.fragmentHoleListRecycler.layoutManager = manager

        binding.fragmentHoleListSrl.setDisableContentWhenRefresh(false)
        binding.fragmentHoleListSrl.setDisableContentWhenLoading(true)

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

        // 监听holeList变化并更新UI
        viewModel.holeList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.errorStatus.observe(viewLifecycleOwner, Observer {
//            Timber.e(it.toString())
            it.message?.let { it -> showToast(it) }
        })

        // 监听刷新状态变化
        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer {
            if(it){
                Timber.e("show refresh")
//                binding.fragmentHoleListSrl.autoRefreshAnimationOnly();//自动刷新，只显示动画不执行刷新
//                showLoading()
            }else{
                Timber.e("hide refresh")
//                dismissLoading()
                binding.fragmentHoleListSrl.finishRefresh(1000)
            }
        })

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
                findNavController()
                    .navigate(HoleViewPagerFragmentDirections.actionNavHoleToNavHoleDetail(pid))
                viewModel.onHoleItemDetailNavigated()
            }
        })
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