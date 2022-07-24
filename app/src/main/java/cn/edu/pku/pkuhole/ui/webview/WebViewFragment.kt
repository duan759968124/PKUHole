package cn.edu.pku.pkuhole.ui.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.navigation.fragment.navArgs
import cn.edu.pku.pkuhole.base.BaseFragment
import cn.edu.pku.pkuhole.data.LocalRepository
import cn.edu.pku.pkuhole.databinding.FragmentWebViewBinding
import timber.log.Timber
import java.net.URLEncoder


class WebViewFragment : BaseFragment() {

    val args: WebViewFragmentArgs by navArgs()
    private lateinit var binding: FragmentWebViewBinding
    private lateinit var mWebViewWV: WebView

    private var moduleName: String = ""
    private var moduleUrl: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWebViewBinding.inflate(inflater, container, false)
        context ?: return binding.root

        moduleName = args.moduleName
//        moduleName = "成绩"
        when (moduleName) {
            "成绩" -> {
                moduleUrl =
                    "http://162.105.209.242/web/score?uid=" + LocalRepository.getUid() + "&password=" + URLEncoder.encode(
                        LocalRepository.getPasswordSecure(),
                        "utf-8"
                    )
//                moduleUrl = "http://162.105.209.242/web/score?uid=01999091&password=" + URLEncoder.encode("b9b0fIJabbdB3pgACl/u+4ic7NKBn3Bx9BH1C70TqvpgmhCR8yaEEKjqGlmaE0GebieZRD4mvrf+IeBey4PEiyzRxfeUTWUUNd7LMVM+OihiKs1/+EMEy1h110Hmg/uFnYHsGuIbXYYwEXD0MEQOi3fi7uwtau1jUQA9yc4F2oSJk27ZKQwR3y4nrbWumETf0l/4ju7ZTosjR4ftVA7IJcTB/nPEYDAewIvLynHd4vzquowXfZvTzy7xCUmPmmVAhV2fgoNobuw1LuS0U6ObzommIlzwf1y7VkYL5R1Fzez1lZEGah82905ZePY/UWsPbAdiCh64YikUqKOrs8x2suGlGmMdLTIxVNjosyr44p5Iy87Bzqg+PrlSFrCRHBNmz9B0i+G6B9zI9JKywfqhIXTUldboAm5sVtbP157JV4r1JsJOihS3aBwChESio1pxc3Byr8idbNT7zBYbo3O5s+KzuhQn/l1MKZPmETdVsjvujarTwAIjVNwX635tesFOGj5sCAyqWNBcoM1PyOqazSzw3eUYyuxiOfzNlk7rIGjpyqhrcYWobY+842Zf/7WMAyeTA8hdrP+RV3/QLQPAnowdjJY7mYvKrgsvgVvQE4wYdzg0e7Fvn1jdBMv9MREszgULb1LNRqA/UmuyQAMhYh1rGSuQhr+lM4njxRV7Ft0=", "utf-8")

            }
            "课表" -> {
                moduleUrl = "https://www.pku.edu.cn"
            }
            else -> {
                showToast("module name and module url is null")
            }
        }

        Timber.e("moduleName %s", moduleName)
        Timber.e("moduleUrl %s", moduleUrl)
//        activity?.actionBar?.title = moduleName
        mWebViewWV = binding.fragmentWebViewContent
        setWebViewSettings()
        mWebViewWV.webViewClient = MyWebViewClient()
//        mWebViewWV.webChromeClient = WebChromeClient()
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        Timber.e("onViewCreated module url %s", moduleUrl)
//
//    }

    override fun initData() {
        if (moduleUrl.isNotEmpty()) {
            moduleUrl.let { mWebViewWV.loadUrl(it) }
        }
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

        // 使用默认的CacheMode就可以实现。也可手动设置缓存模式
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        // 而且数据存储在本地无需经常和服务器进行交互，存储安全、便捷。可用于存储临时的简单数据。作用机制类似于SharedPreference。
        webSettings.domStorageEnabled = true
        //在LOLLIPOP 版本开始需要开启图片混合加载(http 和 https 不开启会导致图片无法加载)
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
//            webSettings.setMixedContentMode(WebSettings.LOAD_NORMAL);
    }

    private class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            Timber.e("WebViewClient  -- onPageStarted  - %s", url)
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {

            super.onPageFinished(view, url)
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            Timber.e("----->onReceivedSslError<-------")
            super.onReceivedSslError(view, handler, error)
        }
    }


}