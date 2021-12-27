package cn.edu.pku.pkuhole.ui.hole

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import cn.edu.pku.pkuhole.adapters.HoleAllListAdapter
import cn.edu.pku.pkuhole.databinding.FragmentHoleAllListBinding
import cn.edu.pku.pkuhole.viewmodels.hole.HoleAllListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HoleAllListFragment : Fragment() {

    private lateinit var binding: FragmentHoleAllListBinding
    private val viewModel: HoleAllListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHoleAllListBinding.inflate(inflater, container, false)
        context ?: return binding.root

//        val application = requireNotNull(this.activity).application
//        val dataSource = AppDatabase.getInstance(application).holeAllListDao()
//        val viewModelFactory = HoleAllListViewModelFactory(dataSource, application)

//        viewModel = ViewModelProvider(this, viewModelFactory)[HoleAllListViewModel::class.java]

//        binding.holeAllListViewModel = viewModel

        val adapter = HoleAllListAdapter()
        binding.holeAllListRecycler.adapter = adapter

        val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        binding.holeAllListRecycler.layoutManager = manager

        viewModel.holeAllList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
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


}