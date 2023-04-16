package cn.edu.pku.treehole.ui.settings

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cn.edu.pku.treehole.NavigationDirections
import cn.edu.pku.treehole.R
import cn.edu.pku.treehole.base.BaseFragment
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.databinding.FragmentSettingsBinding
import cn.edu.pku.treehole.utilities.ClearingDialog
import cn.edu.pku.treehole.utilities.PRIVACY_POLICY_URL
import cn.edu.pku.treehole.utilities.USER_AGREEMENT_URL
import cn.edu.pku.treehole.viewmodels.settings.SettingsViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.azhon.appupdate.manager.DownloadManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var mClearingDialog: ClearingDialog
    private lateinit var manager: DownloadManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
//        viewModel =  ViewModelProvider(this).get(SettingsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mClearingDialog = ClearingDialog(view.context, false)
    }


    override fun initData() {

    }

    override fun initObserve() {
        viewModel.navigationToPrivacyPolicy.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(
                    SettingsFragmentDirections.actionNavSettingsToSimpleWebviewFragment(
                        getString(R.string.privacy_policy), PRIVACY_POLICY_URL
                    )
                )
                viewModel.onNavigateToPrivacyPolicyFinish()
            }
        }

        viewModel.navigateToChangeTextSize.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(SettingsFragmentDirections.actionNavSettingsToNavChangeTextSize())
                viewModel.onNavigateToChangeTextSizeFinish()
            }
        }

        viewModel.navigateToChangeDarkModel.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(SettingsFragmentDirections.actionNavSettingsToNavChangeDarkMode())
                viewModel.onNavigateToChangeDarkModelFinish()
            }
        }

        viewModel.navigateToSetQuoteComment.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(SettingsFragmentDirections.actionNavSettingsToNavSetQuoteComment())
                viewModel.onNavigateToSetQuoteCommentFinish()
            }
        }

        viewModel.navigationToUserAgreement.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(
                    SettingsFragmentDirections.actionNavSettingsToSimpleWebviewFragment(
                        getString(R.string.user_agreement), USER_AGREEMENT_URL
                    )
                )
                viewModel.onNavigateToUserAgreementFinish()
            }
        }

        viewModel.navigationToAboutUs.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(SettingsFragmentDirections.actionNavSettingsToNavAboutUs())
                viewModel.onNavigateToAboutUsFinish()
            }
        }

        viewModel.navigationToCopyright.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(SettingsFragmentDirections.actionNavSettingsToNavCopyright())
                viewModel.onNavigateToCopyrightFinish()
            }
        }

        viewModel.loginStatus.observe(viewLifecycleOwner) { isLogin ->
            if (!isLogin) {
                // 全局导航操作
                findNavController().navigate(R.id.action_global_nav_login)
                viewModel.onNavigateToLoginFinish()
            }
        }

        // 进入人机验证界面
        viewModel.manMachineVerification.observe(viewLifecycleOwner) { isValidate ->
            if (!isValidate.isNullOrEmpty()) {
                // 全局导航操作
                findNavController().navigate(NavigationDirections.actionGlobalNavManMachineVerification(isValidate))
                viewModel.onNavigateToManMachineVerificationFinish()
            }
        }

        // 系统网络报错
        viewModel.errorStatus.observe(viewLifecycleOwner) { error ->
            error.message?.let { showToast("错误-$it") }
        }

        // api报错
        viewModel.failStatus.observe(viewLifecycleOwner) { fail ->
            fail.message?.let { showToast("失败-$it") }
        }

        viewModel.loadingStatus.observe(viewLifecycleOwner) {
            if (it) {
                mClearingDialog.showDialog(mContext, false)
            } else {
                mClearingDialog.dismissDialog()
            }
        }

        viewModel.canUpdate.observe(viewLifecycleOwner) { updateFlag ->
            if (updateFlag == 1) {
//                弹出一个可升级的对话框
                showUpdateDialog()
            }
            if (updateFlag == 0) {
//                弹出一个对话框
                context?.let {
                    MaterialDialog(it).show {
                        title(R.string.update_title)
                        message(R.string.no_update)
                        positiveButton(R.string.confirm)
                    }
                }
            }
        }
    }
    private fun showUpdateDialog() {
        manager = DownloadManager.Builder(activity!!).run {
            LocalRepository.localUpdateInfo!!.app_file_url?.let { apkUrl(it) }
            apkName("PKU_TreeHole.apk")
            smallIcon(R.mipmap.ic_launcher)
            showNewerToast(false)
//            apkVersionCode(1)
            apkVersionCode(LocalRepository.localUpdateInfo!!.app_version_code)
            LocalRepository.localUpdateInfo!!.app_version_name?.let { apkVersionName(it) }
//            apkSize("7.4")
            LocalRepository.localUpdateInfo!!.update_log?.let { apkDescription(it) }
            //apkMD5("DC501F04BBAA458C9DC33008EFED5E7F")

            //flow are unimportant filed
            enableLog(true)
            //httpManager()
            jumpInstallPage(true)
//            dialogImage(R.drawable.ic_dialog)
//            dialogButtonColor(Color.parseColor("#E743DA"))
//            dialogProgressBarColor(Color.parseColor("#E743DA"))
            dialogButtonTextColor(Color.WHITE)
            showNotification(true)
            showBgdToast(false)
            forcedUpgrade(false)
            build()
        }
        manager.download()
    }

}