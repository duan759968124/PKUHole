package cn.edu.pku.pkuhole.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import cn.edu.pku.pkuhole.base.BaseFragment
import cn.edu.pku.pkuhole.databinding.FragmentSettingsBinding
import cn.edu.pku.pkuhole.databinding.FragmentSplashBinding
import cn.edu.pku.pkuhole.viewmodels.SettingsViewModel

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2022/1/5
 * @Desc:
 * @Version:        1.0
 */

class SplashFragment : BaseFragment() {
    private lateinit var binding: FragmentSplashBinding
//    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initObserve() {

    }


    override fun initData() {
        // 是否第一次启动,显示对话框

        // 是否已经登录,是跳转到登录页还是跳转到hole
    }

}