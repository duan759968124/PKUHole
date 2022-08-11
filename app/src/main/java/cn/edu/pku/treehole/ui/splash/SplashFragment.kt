package cn.edu.pku.treehole.ui.splash

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cn.edu.pku.treehole.R
import cn.edu.pku.treehole.base.BaseFragment
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.databinding.FragmentSplashBinding
import cn.edu.pku.treehole.utilities.PRIVACY_POLICY_URL
import cn.edu.pku.treehole.utilities.USER_AGREEMENT_URL
import cn.edu.pku.treehole.viewmodels.SplashViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.azhon.appupdate.manager.DownloadManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2022/1/5
 * @Desc:
 * @Version:        1.0
 */

@AndroidEntryPoint
class SplashFragment : BaseFragment() {
    private lateinit var binding: FragmentSplashBinding
    private val viewModel: SplashViewModel by viewModels()
    private var manager: DownloadManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }


    override fun initData() {
        // 是否第一次启动,显示对话框
//        LocalRepository.setISFirstStart(true)  //测试显示用户协议对话框
        if (LocalRepository.isFirstStart()) {
            // 弹框
            showLaunchWarnDialog()
        } else {
            checkLoginStatus()
        }
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
            checkLoginStatus()
        }
        viewModel.errorStatus.observe(viewLifecycleOwner) { error ->
            error.message?.let { showToast("错误-$it") }
            checkLoginStatus()
        }
        viewModel.canUpdate.observe(viewLifecycleOwner) { updateFlag ->
            if (updateFlag == 1) {
                showUpdateDialog()
            }
            if (updateFlag == 0) {
                checkLoginStatus()
            }
        }
    }

    private fun showUpdateDialog() {
        manager = DownloadManager.Builder(activity!!).run {
            LocalRepository.localUpdateInfo!!.app_file_url?.let { apkUrl(it) }
            apkName("PKU_Hole.apk")
            smallIcon(R.mipmap.ic_launcher)
            showNewerToast(false)
            apkVersionCode(1)
//            apkVersionCode(LocalRepository.localUpdateInfo!!.app_version_code)
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
            forcedUpgrade(true)
//            onDownloadListener(listenerAdapter)
//            onButtonClickListener(this@MainActivity)
            build()
        }
        manager?.download()
    }

    private fun showLaunchWarnDialog() {
//        val launchWarnTextSpan = SpannableStringBuilder()
//        val launchWarnText = getString(R.string.launchWarnText)
//        launchWarnTextSpan.append(launchWarnText);
//        //第一个出现的位置
//        val start = launchWarnText.indexOf("《");
//        Timber.e("%s : index %d  %d", launchWarnText, start, launchWarnText.length)
//        launchWarnTextSpan.setSpan(object : ClickableSpan() {
//            override fun onClick(widget: View) {
//                //用户服务协议点击事件
//                Timber.e("onclick span 服务协议")
//            }
//
//            override fun updateDrawState(ds: TextPaint) {
//                super.updateDrawState(ds)
//                //设置文件颜色
//                ds.color = Color.BLUE
//                // 去掉下划线
//                ds.isUnderlineText = false
//            }
//        }, start, start + 6, 0)
        context?.let {
            MaterialDialog(it).show {
                cancelable(false)
                title(R.string.launchWarnTitle)
                message(R.string.launchWarnText) {
                    html { clickText ->
                        if (clickText == "user_agreement") {
                            dismiss()
                            findNavController().navigate(SplashFragmentDirections.actionNavSplashToSimpleWebviewFragment(
                                getString(R.string.user_agreement), USER_AGREEMENT_URL))

                        }
                        if (clickText == "privacy_policy") {
                            dismiss()
                            findNavController().navigate(SplashFragmentDirections.actionNavSplashToSimpleWebviewFragment(
                                getString(R.string.privacy_policy), PRIVACY_POLICY_URL))

                        }

                    }
                    lineSpacing(1.4f)
                }
                positiveButton(R.string.agree) {
                    LocalRepository.setISFirstStart(false)
//                    checkLoginStatus()
                    // 检查更新
                    viewModel.checkUpdate()
                }
                negativeButton(R.string.reject) {
                    activity?.finish()
                }
            }
        }
    }

    private fun checkLoginStatus() {
        Timber.e("checkLoginStatus")
        if (LocalRepository.getUid().isEmpty()) {
            //未登录，跳转到登录界面
            findNavController().navigate(R.id.action_global_nav_login)
        } else {
            findNavController().navigate(SplashFragmentDirections.actionNavSplashToNavHole())
        }
    }

}