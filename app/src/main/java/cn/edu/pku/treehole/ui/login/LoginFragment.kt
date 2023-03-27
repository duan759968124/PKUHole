package cn.edu.pku.treehole.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cn.edu.pku.treehole.R
import cn.edu.pku.treehole.base.BaseFragment
import cn.edu.pku.treehole.databinding.FragmentLoginBinding
import cn.edu.pku.treehole.utilities.PRIVACY_POLICY_URL
import cn.edu.pku.treehole.utilities.USER_AGREEMENT_URL
import cn.edu.pku.treehole.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 * @Time:           2022/1/4
 * @Desc:
 * @Version:        1.0
 */
@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding
//    private lateinit var validCodeDialog: ValidCodeDialogFragment
    private val userViewModel : UserViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        context ?: return binding.root
        binding.viewModel = userViewModel
        binding.lifecycleOwner = viewLifecycleOwner
//        validCodeDialog = ValidCodeDialogFragment()
        return binding.root

    }

//    private fun navigateToHole() {
//        showToast("登录成功")
//        findNavController()
//            .navigate(LoginFragmentDirections.actionNavLoginToNavHole())
//        userViewModel.onLoginSuccessComplete()
//    }


    override fun initObserve() {
        userViewModel.loadingStatus.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                showLoading()
            } else {
                dismissLoading()
            }
        }
//        userViewModel.loginSuccessNavigation.observe(viewLifecycleOwner) { loginSuccess ->
//            if (loginSuccess)
////                navigateToHole()
////                showToast("login success")
//        }

        userViewModel.showInputValidCode.observe(viewLifecycleOwner) { showInputValidCode ->
            if (showInputValidCode) {
                findNavController()
                    .navigate(LoginFragmentDirections.actionNavLoginToNavInputValidCode())
                // 显示对话框
//                validCodeDialog.show(parentFragmentManager, "dialog")
                userViewModel.onNavigateToInputValidCodeComplete()
            }
//            if (showInputValidCode) {
//                showToast("登录成功")
//                findNavController().navigate(LoginFragmentDirections.actionNavLoginToNavHole())
//                userViewModel.onNavigateToInputValidCodeComplete()
//            }
        }

        userViewModel.failStatus.observe(viewLifecycleOwner) { fail ->
            fail.message?.let { showToast("失败-$it") }
        }

        userViewModel.errorStatus.observe(viewLifecycleOwner) { error ->
            error.message?.let { showToast("错误-$it") }
        }

        userViewModel.loginInfoIsNull.observe(viewLifecycleOwner) { isNull ->
            if (isNull) {
                showToast("账号或者密码不能为空！")
            }
        }

        userViewModel.navigationToPrivacyPolicy.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(LoginFragmentDirections.actionNavLoginToNavSimpleWebView2(
                    getString(R.string.privacy_policy), PRIVACY_POLICY_URL))
                userViewModel.onNavigateToPrivacyPolicyFinish()
            }
        }

        userViewModel.navigationToUserAgreement.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(LoginFragmentDirections.actionNavLoginToNavSimpleWebView2(
                    getString(R.string.user_agreement), USER_AGREEMENT_URL))
                userViewModel.onNavigateToUserAgreementFinish()
            }
        }
    }


    override fun initData() {
        userViewModel.initData()
//        val navController = findNavController()
//        val savedStateHandle = navController.previousBackStackEntry!!.savedStateHandle
//
//        savedStateHandle.set(LOGIB_SUCCESSFUL, false)
//
//        userRepository.addLoginSuccessListener{
//            savedStateHandle.set(LOGIN_SUCCESSFUL ,true)
//            navController.popBackStack()
//        }
    }

}