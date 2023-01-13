package cn.edu.pku.treehole.ui.settings

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cn.edu.pku.treehole.R
import cn.edu.pku.treehole.base.BaseFragment
import cn.edu.pku.treehole.databinding.FragmentChangeTextSizeBinding
import cn.edu.pku.treehole.viewmodels.ChangeTextSizeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeTextSizeFragment : BaseFragment() {

    private lateinit var binding: FragmentChangeTextSizeBinding
    private val viewModel: ChangeTextSizeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeTextSizeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this;
        binding.viewModel = viewModel
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_change_text_size_toolbar, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.button_finish -> {
                viewModel.finishSetTextSize()
                findNavController().navigate(ChangeTextSizeFragmentDirections.actionNavChangeTextSizeToNavSettings())
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun initData() {

    }

    override fun initObserve() {

    }

}