package cn.edu.pku.treehole.ui.login

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cn.edu.pku.treehole.NavigationDirections
import cn.edu.pku.treehole.R
import cn.edu.pku.treehole.base.BaseFragment
import cn.edu.pku.treehole.databinding.FragmentInputValidCodeBinding
import cn.edu.pku.treehole.viewmodels.UserViewModel
import com.fraggjkee.smsconfirmationview.SmsConfirmationView
import dagger.hilt.android.AndroidEntryPoint


/**
 *
 * @Time:           2022/8/10
 * @Desc:
 * @Version:        1.0
 */
@AndroidEntryPoint
class InputValidCodeFragment : BaseFragment() {

    private lateinit var binding: FragmentInputValidCodeBinding
    private val userViewModel: UserViewModel by viewModels()
    var isCompleteInput: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentInputValidCodeBinding.inflate(inflater, container, false)
        context ?: return binding.root
        val imm = context!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        binding.validCodeView.onChangeListener =
            SmsConfirmationView.OnChangeListener { code, isComplete ->
                isCompleteInput = isComplete
                if(isCompleteInput){
                    imm.hideSoftInputFromWindow(activity!!.window.decorView.windowToken, 0)
                }
                userViewModel.inputStatus(isCompleteInput, code)
            }
        binding.validCodeView.startListeningForIncomingMessages()

        binding.viewModel = userViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root

    }

    override fun initData() {
    }

    override fun initObserve() {
        userViewModel.sendValidCodeSuccess.observe(viewLifecycleOwner) {
            if (it) {
                showToast("验证码发送成功")
                binding.btnGetCode.isClickable = false
                binding.btnGetCode.setBackgroundColor(resources.getColor(R.color.gray_500))
                binding.btnGetCode.text = "已发送"
                userViewModel.finishSendValidCode()
            }
        }

        userViewModel.clickVerifyCode.observe(viewLifecycleOwner) {
            if (it && !isCompleteInput) {
                showToast("请完整输入验证码")
                userViewModel.finishInputIncompleteToast()
            }
        }
        userViewModel.loadingStatus.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                showLoading()
            } else {
                dismissLoading()
            }
        }
        userViewModel.failStatus.observe(viewLifecycleOwner) { fail ->
            fail.message?.let { showToast("失败-$it") }
        }

        userViewModel.errorStatus.observe(viewLifecycleOwner) { error ->
            error.message?.let { showToast("错误-$it") }
        }

        userViewModel.verifySuccessNavigation.observe(viewLifecycleOwner) { verifySuccessNavigation ->
            if (verifySuccessNavigation) {
                showToast("登录成功")
                findNavController().navigate(InputValidCodeFragmentDirections.actionNavInputValidCodeToNavHole())
                userViewModel.onVerifySuccessComplete()
            }
        }

        // 进入人机验证界面
        userViewModel.manMachineVerification.observe(viewLifecycleOwner) { isValidate ->
            if (!isValidate.isNullOrEmpty()) {
                // 全局导航操作
                findNavController().navigate(NavigationDirections.actionGlobalNavManMachineVerification(isValidate))
                userViewModel.onNavigateToLoginFinish()
            }
        }
    }


}