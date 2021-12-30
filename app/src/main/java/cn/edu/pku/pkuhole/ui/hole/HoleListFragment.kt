package cn.edu.pku.pkuhole.ui.hole

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import cn.edu.pku.pkuhole.adapters.HoleListAdapter
import cn.edu.pku.pkuhole.base.BaseFragment
import cn.edu.pku.pkuhole.base.network.IStateObserver
import cn.edu.pku.pkuhole.data.hole.HoleListItemBean
import cn.edu.pku.pkuhole.databinding.FragmentHoleListBinding
import cn.edu.pku.pkuhole.viewmodels.hole.HoleListViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HoleListFragment : BaseFragment() {

    private lateinit var binding: FragmentHoleListBinding
    private val viewModel: HoleListViewModel by viewModels()

    @SuppressLint("TimberArgCount")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHoleListBinding.inflate(inflater, container, false)
        context ?: return binding.root

//        val application = requireNotNull(this.activity).application
//        val dataSource = AppDatabase.getInstance(application).holeAllListDao()
//        val viewModelFactory = HoleAllListViewModelFactory(dataSource, application)

//        viewModel = ViewModelProvider(this, viewModelFactory)[HoleAllListViewModel::class.java]

//        binding.holeAllListViewModel = viewModel

        val adapter = HoleListAdapter()
        binding.holeListRecycler.adapter = adapter

        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        binding.holeListRecycler.layoutManager = manager

        // 监听holeList变化并更新UI
        viewModel.holeList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.errorLiveData.observe(viewLifecycleOwner, Observer {
//            Timber.e(it.toString())
            it.message?.let { it1 -> showToast(it1) }
        })

        viewModel.loadingLiveData.observe(viewLifecycleOwner, Observer {
            if(it){
                Timber.e("show loading")
                showLoading()
            }else{
                Timber.e("hide loading")
                dismissLoading()
            }
        })

//        viewModel.navigationToHoleItemDetail.observe(viewLifecycleOwner, Observer { pid ->
//            pid?.let {
//                findNavController()
//                    .navigate(HoleViewPagerFragmentDirections.actionNavHoleToNavHoleDetail(pid))
//                viewModel.onHoleItemDetailNavigated()
//            }
//        })
//        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
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