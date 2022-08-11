package cn.edu.pku.treehole.ui.hole

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import cn.edu.pku.treehole.base.BaseFragment
import cn.edu.pku.treehole.databinding.FragmentPreviewPictureBinding
import cn.edu.pku.treehole.viewmodels.hole.PreviewPictureViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewPictureFragment : BaseFragment() {

    private lateinit var binding: FragmentPreviewPictureBinding
    private val viewModel: PreviewPictureViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPreviewPictureBinding.inflate(inflater, container, false)
        context ?: return binding.root
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun initData() {

    }



}