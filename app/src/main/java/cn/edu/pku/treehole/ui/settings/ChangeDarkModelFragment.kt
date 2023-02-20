package cn.edu.pku.treehole.ui.settings

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cn.edu.pku.treehole.R
import cn.edu.pku.treehole.base.BaseFragment
import cn.edu.pku.treehole.databinding.FragmentChangeDarkModelBinding
import cn.edu.pku.treehole.viewmodels.ChangeDarkModelViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeDarkModelFragment : BaseFragment() {

    private lateinit var binding: FragmentChangeDarkModelBinding
    private val viewModel: ChangeDarkModelViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeDarkModelBinding.inflate(inflater, container, false)
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
//                viewModel.finishSetDarkModel()
//                findNavController().navigate(ChangeDarkModelFragmentDirections.actionNavChangeTextSizeToNavSettings())
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