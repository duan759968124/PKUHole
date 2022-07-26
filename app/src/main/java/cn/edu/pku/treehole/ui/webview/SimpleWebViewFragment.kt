package cn.edu.pku.treehole.ui.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.navigation.fragment.navArgs
import cn.edu.pku.treehole.base.BaseFragment
import cn.edu.pku.treehole.databinding.FragmentWebViewBinding
import timber.log.Timber


class SimpleWebViewFragment : BaseFragment() {

    val args: SimpleWebViewFragmentArgs by navArgs()
    private lateinit var binding: FragmentWebViewBinding
    private lateinit var mWebViewWV: WebView
//    private lateinit var mWebViewClient: WebViewClient
//    private lateinit var mWebChromeClient: WebChromeClient
    private var moduleName: String = ""
    private var moduleUrl: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebViewBinding.inflate(inflater, container, false)
        context ?: return binding.root

        moduleName = args.moduleName
        moduleUrl = args.moduleUrl

        Timber.e("moduleName %s", moduleName)
        Timber.e("moduleUrl %s", moduleUrl)
//        activity?.actionBar?.title = moduleName
        mWebViewWV = binding.fragmentWebViewContent
        setWebViewSettings()
//        mWebViewWV.webViewClient = WebViewClient()
//        mWebViewWV.webChromeClient = WebChromeClient()
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        Timber.e("onViewCreated module url %s", moduleUrl)
//
//    }

    override fun initData() {
        if(moduleUrl.isNotEmpty()){
            moduleUrl.let { mWebViewWV.loadUrl(it) }
        }
//        mWebViewWV.clearHistory()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebViewSettings() {
        val webSettings: WebSettings = mWebViewWV.settings
        //禁止页面放大
        webSettings.loadWithOverviewMode = true
        // 自适应屏幕
        webSettings.useWideViewPort = true
        // 设置WebView是否可以运行JavaScript
        webSettings.javaScriptEnabled = true
        // 设置WebView是否可以由JavaScript自动打开窗口，默认为false，通常与JavaScript的window.open()配合使用
        webSettings.javaScriptCanOpenWindowsAutomatically = true
    }

//    private fun getWebViewClient(): WebViewClient {
//        return mWebViewClient
//    }
//
//    private fun getWebChromeClient(): WebChromeClient? {
//        return mWebChromeClient
//    }

}