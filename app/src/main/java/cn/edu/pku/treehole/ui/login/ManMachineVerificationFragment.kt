package cn.edu.pku.treehole.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cn.edu.pku.treehole.NavigationDirections
import cn.edu.pku.treehole.R
import cn.edu.pku.treehole.base.BaseFragment
import cn.edu.pku.treehole.databinding.FragmentLoginBinding
import cn.edu.pku.treehole.databinding.FragmentManMachineVerificationBinding
import cn.edu.pku.treehole.utilities.PRIVACY_POLICY_URL
import cn.edu.pku.treehole.utilities.USER_AGREEMENT_URL
import cn.edu.pku.treehole.viewmodels.ManMachineVerificationViewModel
import cn.edu.pku.treehole.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 *
 * @Time:           2022/1/4
 * @Desc:
 * @Version:        1.0
 */
@AndroidEntryPoint
class ManMachineVerificationFragment : BaseFragment() {

    private lateinit var binding: FragmentManMachineVerificationBinding
    private val viewModel : ManMachineVerificationViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentManMachineVerificationBinding.inflate(inflater, container, false)
        context ?: return binding.root
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root

    }


    override fun initObserve() {
        viewModel.loadingStatus.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                showLoading()
            } else {
                dismissLoading()
            }
        }

        viewModel.failStatus.observe(viewLifecycleOwner) { fail ->
            fail.message?.let { showToast("失败-$it") }
        }

        viewModel.errorStatus.observe(viewLifecycleOwner) { error ->
            error.message?.let { showToast("错误-$it") }
        }

        // 退出到login界面
        viewModel.loginStatus.observe(viewLifecycleOwner) { isLogin ->
            Timber.e("isLogin %s", isLogin)
            if (!isLogin) {
                // 全局导航操作
                findNavController().navigate(R.id.action_global_nav_login)
                viewModel.onNavigateToLoginFinish()
            }
        }

        viewModel.verifyCodeIsNull.observe(viewLifecycleOwner) { inputNull ->
            if (inputNull)
                showToast("验证信息为空，请重新输入！")
        }


        viewModel.verifySuccessNavigation.observe(viewLifecycleOwner){
            if(it){
                //跳转到首页
                findNavController().navigate(R.id.nav_hole)
                //跳转上一页
//                findNavController().navigateUp()
                viewModel.onVerifySuccessComplete()
            }
        }
    }


    override fun initData() {

    }

}