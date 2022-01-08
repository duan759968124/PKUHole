package cn.edu.pku.pkuhole.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.edu.pku.pkuhole.base.BaseViewModel
import cn.edu.pku.pkuhole.base.network.ApiException
import cn.edu.pku.pkuhole.data.UserInfo
import cn.edu.pku.pkuhole.data.LocalRepository
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
 * @Desc:           loginFragment与mainActivity共用这个viewModel
 * @Version:        1.0
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    holeRepository: HoleRepository,
) : BaseViewModel(holeRepository=holeRepository) {

    var userInfo = MutableLiveData<UserInfo>()
    var account = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    // loginFragment监听 请求状态 true 跳转到nav_hole
    private val _loginSuccessNavigation = MutableLiveData<Boolean>()
    val loginSuccessNavigation: LiveData<Boolean>
        get() = _loginSuccessNavigation


    init {
        account.value = LocalRepository.getAccount()
        password.value = LocalRepository.getPassword()
    }

    fun login() {
        // Todo: 做一个账号密码检查
        Timber.e("click login account %s password %s", account.value, password.value)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadingStatus.postValue(true)
                val response =
                    database.login(account = account.value!!, password = password.value!!)
                Timber.e("login response %s", response)
                _loginSuccessNavigation.postValue(true)
                val userInfoRes = UserInfo(
                    uid = response.uid!!,
                    name = response.name,
                    department = response.department,
                    token = response.token,
                    token_timestamp = response.token_timestamp
                )
                // 用来更新activity中显示
                userInfo.postValue(userInfoRes)
                // 将数据存到本地
                LocalRepository.setUserInfo(userInfoRes)
                LocalRepository.setAccount(account = account.value!!)
                LocalRepository.setPassword(password = password.value!!)
            } catch (e: Exception) {
                when (e) {
                    is ApiException -> failStatus.postValue(e)
                    else -> errorStatus.postValue(e)
                }
            } finally {
                loadingStatus.postValue(false)
                Timber.e("loginFinish %s", LocalRepository.getUserInfo().toString())
            }
        }
    }


    // login Fragment持有
    fun onLoginSuccessComplete() {
        _loginSuccessNavigation.value = false
    }

    // mainActivity检查登录状态
    fun checkLoginStatus() {
        userInfo.value = UserInfo("", "", "", "", 0L)
        Timber.e("checkLoginStatus")
        Timber.e("uid %s", LocalRepository.getUid())
        if(LocalRepository.getUid().isEmpty()){
            //未登录，跳转到登录界面
            _loginStatus.value = false
        }
        else{
            userInfo.value = LocalRepository.getUserInfo()
        }
    }

    // mainActivity持有
//    fun onNavigateToLoginFinish() {
//        _loginStatus.value = true
//    }
}