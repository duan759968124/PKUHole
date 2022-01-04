package cn.edu.pku.pkuhole.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import cn.edu.pku.pkuhole.base.BaseFragment
import cn.edu.pku.pkuhole.databinding.FragmentLoginBinding
import cn.edu.pku.pkuhole.ui.hole.HoleViewPagerFragmentDirections
import cn.edu.pku.pkuhole.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2022/1/4
 * @Desc:
 * @Version:        1.0
 */
@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding

    private val userViewModel : UserViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        context ?: return binding.root
        binding.viewModel = userViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        userViewModel.loadingStatus.observe(viewLifecycleOwner, Observer { loading ->
            if(loading){
                showLoading()
            }else{
                dismissLoading()
            }
        })

        userViewModel.loginSuccessNavigation.observe(viewLifecycleOwner, Observer { loginSuccess ->
            if(loginSuccess)
                navigateToHole()
        })

        return binding.root

    }

    private fun navigateToHole() {
        showToast("登录成功")
        findNavController()
            .navigate(LoginFragmentDirections.actionNavLoginToNavHole())
        userViewModel.onLoginSuccessComplete()
    }


    override fun initData() {

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