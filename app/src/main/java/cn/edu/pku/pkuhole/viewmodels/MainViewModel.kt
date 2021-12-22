package cn.edu.pku.pkuhole.viewmodels

import androidx.lifecycle.ViewModel
import cn.edu.pku.pkuhole.data.UserInfo
import timber.log.Timber

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */
class MainViewModel : ViewModel() {

    var userInfo: UserInfo = UserInfo("Test", "CCPKU-viewModel")

    init {
        Timber.e("Main View Model init")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.e("Main View Model onCleared")
    }
}