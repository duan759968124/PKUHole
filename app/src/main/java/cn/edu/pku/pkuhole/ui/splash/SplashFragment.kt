package cn.edu.pku.pkuhole.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import cn.edu.pku.pkuhole.base.BaseFragment
import cn.edu.pku.pkuhole.databinding.FragmentSettingsBinding
import cn.edu.pku.pkuhole.viewmodels.SettingsViewModel

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2022/1/5
 * @Desc:
 * @Version:        1.0
 */
class splashFragment : BaseFragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)
//        viewModel =  ViewModelProvider(this).get(SettingsViewModel::class.java)
        return binding.root
    }

    override fun initObserve() {

    }


    override fun initData() {

    }

}