package cn.edu.pku.treehole.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import cn.edu.pku.treehole.R
import cn.edu.pku.treehole.base.BaseFragment
import cn.edu.pku.treehole.databinding.FragmentSettingsBinding
import cn.edu.pku.treehole.utilities.ClearingDialog
import cn.edu.pku.treehole.utilities.LoadingDialog
import cn.edu.pku.treehole.utilities.PRIVACY_POLICY_URL
import cn.edu.pku.treehole.utilities.USER_AGREEMENT_URL
import cn.edu.pku.treehole.viewmodels.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.concurrent.schedule

@AndroidEntryPoint
class SettingsFragment : BaseFragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var mClearingDialog: ClearingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
//        viewModel =  ViewModelProvider(this).get(SettingsViewModel::class.java)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mClearingDialog = ClearingDialog(view.context, false)
    }


    override fun initData() {

    }

    override fun initObserve() {

        viewModel.navigationToPrivacyPolicy.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(SettingsFragmentDirections.actionNavSettingsToSimpleWebviewFragment(
                    getString(R.string.privacy_policy), PRIVACY_POLICY_URL))
                viewModel.onNavigateToPrivacyPolicyFinish()
            }
        })

        viewModel.navigateToChangeTextSize.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(SettingsFragmentDirections.actionNavSettingsToNavChangeTextSize())
                viewModel.onNavigateToChangeTextSizeFinish()
            }
        })

        viewModel.navigationToUserAgreement.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(SettingsFragmentDirections.actionNavSettingsToSimpleWebviewFragment(
                    getString(R.string.user_agreement), USER_AGREEMENT_URL))
                viewModel.onNavigateToUserAgreementFinish()
            }
        })

        viewModel.navigationToAboutUs.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(SettingsFragmentDirections.actionNavSettingsToNavAboutUs())
                viewModel.onNavigateToAboutUsFinish()
            }
        })

        viewModel.navigationToCopyright.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(SettingsFragmentDirections.actionNavSettingsToNavCopyright())
                viewModel.onNavigateToCopyrightFinish()
            }
        })

        viewModel.loginStatus.observe(viewLifecycleOwner, Observer { isLogin ->
            if (!isLogin) {
                // 全局导航操作
                findNavController().navigate(R.id.action_global_nav_login)
                viewModel.onNavigateToLoginFinish()
            }
        })

        // 系统网络报错
        viewModel.errorStatus.observe(viewLifecycleOwner, Observer { error ->
            error.message?.let { showToast("错误-$it") }
        })

        // api报错
        viewModel.failStatus.observe(viewLifecycleOwner, Observer { fail ->
            fail.message?.let { showToast("失败-$it") }
        })

        viewModel.loadingStatus.observe(viewLifecycleOwner){
            if(it){
                mClearingDialog.showDialog(mContext, false)
            } else {
                mClearingDialog.dismissDialog()
            }
        }

    }

}