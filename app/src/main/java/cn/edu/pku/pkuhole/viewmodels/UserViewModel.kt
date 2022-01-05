package cn.edu.pku.pkuhole.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.edu.pku.pkuhole.base.BaseViewModel
import cn.edu.pku.pkuhole.base.network.HoleApiException
import cn.edu.pku.pkuhole.data.UserInfo
import cn.edu.pku.pkuhole.data.UserInfoRepository
import cn.edu.pku.pkuhole.data.hole.HoleRepository
import cn.edu.pku.pkuhole.utilities.kv
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
    var account = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    private val _loginSuccessNavigation = MutableLiveData<Boolean>()
    val loginSuccessNavigation: LiveData<Boolean>
        get() = _loginSuccessNavigation

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean>
        get() = _loginStatus


    init {
        account.value = "1906194042"
        password.value = "qhd1230it"
    }

    fun login() {
        Timber.e("click login account %s password %s", account.value, password.value)
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                loadingStatus.postValue(true)
                val response =
                    database.login(account = account.value!!, password = password.value!!)
                Timber.e("login response %s", response)
                //Todo: 存数据 key-value存储
                _loginSuccessNavigation.postValue(true)
                userInfo.postValue(UserInfo(
                                uid = response.uid!!,
                                name = response.name,
                                department = response.department,
                                token = response.token,
                                token_timestamp = response.token_timestamp
                            ))
                // 将数据存到本地
                UserInfoRepository.setUserInfo(userInfo.value!!)
            } catch (e: Exception) {
                when (e) {
                    is HoleApiException -> failStatus.postValue(e)
                    else -> errorStatus.postValue(e)
                }
            } finally {
//                loadingStatus.postValue(false)
            }
        }
    }


    fun onLoginSuccessComplete() {
        _loginSuccessNavigation.value = false
    }

    // mainActivity检查登录状态
    fun checkLoginStatus() {
        if(UserInfoRepository.getUid().isEmpty()){
            //未登录，跳转到登录界面
            _loginStatus.value = false
        }else{
            userInfo.value = UserInfoRepository.getUserInfo()
        }
    }

    fun onNavigateToLoginFinish() {
        _loginStatus.value = true
    }
}