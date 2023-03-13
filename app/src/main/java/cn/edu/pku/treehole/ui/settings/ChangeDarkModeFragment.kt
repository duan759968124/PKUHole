package cn.edu.pku.treehole.ui.settings

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cn.edu.pku.treehole.R
import cn.edu.pku.treehole.base.BaseFragment
import cn.edu.pku.treehole.databinding.FragmentChangeDarkModeBinding
import cn.edu.pku.treehole.viewmodels.settings.ChangeDarkModeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeDarkModeFragment : BaseFragment() {

    private lateinit var binding: FragmentChangeDarkModeBinding
    private val viewModel: ChangeDarkModeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeDarkModeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this;
        binding.viewModel = viewModel
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_finish, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.button_finish -> {
                viewModel.finishChangeDarkMode()
                findNavController().navigate(ChangeDarkModeFragmentDirections.actionNavChangeDarkModeToNavSettings())
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