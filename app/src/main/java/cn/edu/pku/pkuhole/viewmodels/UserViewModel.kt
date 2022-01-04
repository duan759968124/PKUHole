package cn.edu.pku.pkuhole.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.edu.pku.pkuhole.base.BaseViewModel
import cn.edu.pku.pkuhole.base.network.HoleApiException
import cn.edu.pku.pkuhole.data.UserInfo
import cn.edu.pku.pkuhole.data.hole.HoleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    holeRepository: HoleRepository,
) : BaseViewModel() {

    var userInfo = MutableLiveData<UserInfo>()
    private val database = holeRepository
    val account = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _loginSuccessNavigation = MutableLiveData<Boolean>()
    val loginSuccessNavigation: LiveData<Boolean>
        get() = _loginSuccessNavigation


    init {
        account.value = "1906194042"
        password.value = "qhd1230its"
    }

    fun login() {
        Timber.e("click login account %s password %s", account.value, password.value)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadingStatus.postValue(true)
                val response =
                    account.value?.let { password.value?.let { it1 -> database.login(it, it1) } }
                Timber.e("login response %s", response)
                if (response != null) {
                    if (response.code == 0) {
                        //Todo: 存数据 key-value存储
                        _loginSuccessNavigation.postValue(true)
                        userInfo.postValue(response.uid?.let {
                            response.token?.let { it1 ->
                                response.token_timestamp?.let { it2 ->
                                    UserInfo(uid = it,
                                        name = response.name,
                                        department = response.department,
                                        token = it1,
                                        token_timestamp = it2)
                                }
                            }
                        })
                    } else {
                        // Todo：在repository中改为抛出异常处理
                        throw HoleApiException(response.code, response.msg)
                    }
                }
            } catch (e: Exception) {
                errorStatus.postValue(e)
                e.message?.let { Timber.e(e.message) }
            } finally {
                loadingStatus.postValue(false)
            }
        }
    }


    fun onLoginSuccessComplete() {
        _loginSuccessNavigation.value = false
    }
}