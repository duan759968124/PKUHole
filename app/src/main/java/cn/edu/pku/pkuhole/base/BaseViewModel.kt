package cn.edu.pku.pkuhole.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.edu.pku.pkuhole.base.network.HoleApiException
import cn.edu.pku.pkuhole.utilities.SingleLiveData

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/28
 * @Desc:
 * @Version:        1.0
 */
open class BaseViewModel: ViewModel() {
    // 加载更多和请求加载等待状态
    val loadingStatus = MutableLiveData<Boolean>()

    // 刷新等待状态
    val refreshStatus = MutableLiveData<Boolean>()
    // error 变量
//    val errorStatus = MutableLiveData<Throwable>()
//    网络异常错误监听【只需要toast的异常】
    val errorStatus = SingleLiveData<Throwable>()
//    API错误监听【需要退出到登陆的异常】
    val failStatus = SingleLiveData<HoleApiException>()


}