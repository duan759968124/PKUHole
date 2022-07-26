package cn.edu.pku.treehole.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.edu.pku.treehole.BuildConfig
import cn.edu.pku.treehole.base.BaseFragment
import cn.edu.pku.treehole.databinding.FragmentAboutUsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutUsFragment : BaseFragment() {

    private lateinit var binding: FragmentAboutUsBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        binding.fragmentAboutUsVersionTv.text = "北大树洞 " + BuildConfig.VERSION_NAME
        return binding.root
    }

    override fun initData() {

    }

}