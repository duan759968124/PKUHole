package cn.edu.pku.treehole.ui.settings

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import cn.edu.pku.treehole.base.BaseFragment
import cn.edu.pku.treehole.databinding.FragmentSetQuoteCommentBinding
import cn.edu.pku.treehole.viewmodels.settings.SetQuoteCommentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetQuoteCommentFragment : BaseFragment() {

    private lateinit var binding: FragmentSetQuoteCommentBinding
    private val viewModel: SetQuoteCommentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetQuoteCommentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this;
        binding.viewModel = viewModel
//        setHasOptionsMenu(true)
        return binding.root
    }

    override fun initData() {

    }

    override fun initObserve() {

    }

}