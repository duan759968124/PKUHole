package cn.edu.pku.pkuhole.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.edu.pku.pkuhole.api.HoleApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.Flow

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/28
 * @Desc:
 * @Version:        1.0
 */
open class BaseViewModel: ViewModel() {
    val loadingLiveData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<Throwable>()


}