package cn.edu.pku.pkuhole.ui.splash

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import cn.edu.pku.pkuhole.R
import cn.edu.pku.pkuhole.base.BaseFragment
import cn.edu.pku.pkuhole.data.LocalRepository
import cn.edu.pku.pkuhole.databinding.FragmentSplashBinding
import com.afollestad.materialdialogs.MaterialDialog
import timber.log.Timber
import android.graphics.Color

import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.widget.TextView


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
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun initData() {
        // 是否第一次启动,显示对话框
        LocalRepository.setISFirstStart(true)
        if (LocalRepository.isFirstStart()) {
            Timber.e("isFirstStart true")
            // 弹框
            showLaunchWarnDialog()
        } else {
            Timber.e("isFirstStart false")
            checkLoginStatus()
        }
    }

    private fun showLaunchWarnDialog() {
        val launchWarnTextSpan = SpannableStringBuilder()
        val launchWarnText = getString(R.string.launchWarnText)
        launchWarnTextSpan.append(launchWarnText);
        //第一个出现的位置
        val start = launchWarnText.indexOf("《");
        Timber.e("%s : index %d  %d", launchWarnText, start, launchWarnText.length)
        launchWarnTextSpan.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                //用户服务协议点击事件
                Timber.e("onclick span 服务协议")
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                //设置文件颜色
                ds.color = Color.BLUE
                // 去掉下划线
                ds.isUnderlineText = false
            }
        }, start, start + 6, 0)
        // Todo: 更换dialog
        context?.let {
            MaterialDialog(it).show {
                cancelable(false)
                title(R.string.launchWarnTitle)
                message(R.string.launchWarnText) {
                    html{ clickText -> Timber.e("Clicked link: $clickText")}
                    lineSpacing(1.4f)
                }
                positiveButton(R.string.agree) {
                    LocalRepository.setISFirstStart(false)
                    checkLoginStatus()
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