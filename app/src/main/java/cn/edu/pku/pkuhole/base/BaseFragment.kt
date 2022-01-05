package cn.edu.pku.pkuhole.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.edu.pku.pkuhole.utilities.LoadingDialog

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/29
 * @Desc:
 * @Version:        1.0
 */
abstract class BaseFragment: Fragment() {

    private lateinit var mLoadingDialog: LoadingDialog
    private lateinit var mContext: Context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLoadingDialog = LoadingDialog(view.context, false)
        initObserve()
        initData()
    }

    abstract fun initObserve()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    abstract fun initData()

    /**
     * show 加载中
     */
    fun showLoading() {
        mLoadingDialog.showDialog(mContext, false)
    }

    /**
     * dismiss loading dialog
     */
    fun dismissLoading() {
        mLoadingDialog.dismissDialog()
    }

    private var time: Long = 0
    private var oldMsg: String? = null

    /**
     * 相同msg 只显示一个。
     */
    fun showToast(msg: String) {
        if (msg != oldMsg) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
            time = System.currentTimeMillis()
        } else {
            if (System.currentTimeMillis() - time > 2000) {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
                time = System.currentTimeMillis()
            }
        }
        oldMsg = msg
    }
}