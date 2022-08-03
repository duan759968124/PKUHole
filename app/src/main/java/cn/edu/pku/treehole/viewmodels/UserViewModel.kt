package cn.edu.pku.treehole.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.base.network.ApiException
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.UserInfo
import cn.edu.pku.treehole.data.hole.HoleRepository
import cn.edu.pku.treehole.utilities.EncryptUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
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
) : BaseViewModel(holeRepository = holeRepository) {

    var userInfo = MutableLiveData<UserInfo>()
    var account = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var passwordSecure = MutableLiveData<String>()

    var agreementStatus = MutableLiveData<Boolean>().apply { value = false }

    private val _loginInfoIsNull = MutableLiveData<Boolean>()
    val loginInfoIsNull: LiveData<Boolean>
        get() = _loginInfoIsNull

    // loginFragment监听 请求状态 true 跳转到nav_hole
    private val _loginSuccessNavigation = MutableLiveData<Boolean>()
    val loginSuccessNavigation: LiveData<Boolean>
        get() = _loginSuccessNavigation


    init {

//        account.value = "1906194042"
//        password.value = ""
    }

    fun loginSecure() {
        if (account.value.isNullOrEmpty() or password.value.isNullOrEmpty()) {
            Timber.e("account or password is null")
            _loginInfoIsNull.value = true
        } else {
            _loginInfoIsNull.value = false
            // 对密码加密
            // kotlin 加密
            passwordSecure.value = password.value?.let {
                EncryptUtils.encrypt(it.trim(),
                    EncryptUtils.getPublicKeyFromString(EncryptUtils.key_Pub))
            }
            // java 加密
//            val publicKey: RSAPublicKey = getPublicKeyFromString(key_Pub)
//            passwordSecure.value = encrypt(password.value, publicKey)
            Timber.e("click password secure %s", passwordSecure.value)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    loadingStatus.postValue(true)
                    val response =
                        database.loginSecure(
                            account = account.value!!,
                            passwordSecure = passwordSecure.value!!
                        )
                    Timber.e("login response %s", response)
                    _loginSuccessNavigation.postValue(true)
//                    val userInfoRes = UserInfo(
//                        uid = response.uid!!,
//                        name = response.name,
//                        department = response.department,
//                        token = response.token,
//                        token_timestamp = response.token_timestamp
//                    )
                    // 数据存到本地
                    LocalRepository.localUserInfo = response.data
                    response.data?.let { LocalRepository.setUid(it.uid) }
                    LocalRepository.localJwtTimestamp = response.timestamp!!
                    LocalRepository.setAccount(account = account.value!!)
                    LocalRepository.setPasswordSecure(pwdSecure = passwordSecure.value!!)
                    // 用来更新activity中显示
                    userInfo.postValue(LocalRepository.localUserInfo)
                } catch (e: Exception) {
                    when (e) {
                        is ApiException -> failStatus.postValue(e)
                        else -> errorStatus.postValue(e)
                    }
                } finally {
                    loadingStatus.postValue(false)
//                    Timber.e("loginFinish %s", LocalRepository.getUserInfo().toString())
                }
            }
        }
    }

//    fun login() {
//        if (account.value.isNullOrEmpty() or password.value.isNullOrEmpty()) {
//            Timber.e("account or password is null")
//        } else { // 对密码加密
//            Timber.e("click login account %s password %s", account.value, password.value)
//            viewModelScope.launch(Dispatchers.IO) {
//                try {
//                    loadingStatus.postValue(true)
//                    val response =
//                        database.login(account = account.value!!, password = password.value!!)
//                    Timber.e("login response %s", response)
//                    _loginSuccessNavigation.postValue(true)
//                    val userInfoRes = UserInfo(
//                        uid = response.uid!!,
//                        name = response.name,
//                        department = response.department,
//                        token = response.token,
//                        token_timestamp = response.token_timestamp
//                    )
//                    // 用来更新activity中显示
//                    userInfo.postValue(userInfoRes)
//                    // 将数据存到本地
//                    LocalRepository.setUserInfo(userInfoRes)
//                    LocalRepository.setAccount(account = account.value!!)
//                    LocalRepository.setPassword(password = password.value!!)
//                } catch (e: Exception) {
//                    when (e) {
//                        is ApiException -> failStatus.postValue(e)
//                        else -> errorStatus.postValue(e)
//                    }
//                } finally {
//                    loadingStatus.postValue(false)
//                    Timber.e("loginFinish %s", LocalRepository.getUserInfo().toString())
//                }
//            }
//        }
//    }


    private var _navigationToPrivacyPolicy = MutableLiveData<Boolean>()
    val navigationToPrivacyPolicy: LiveData<Boolean>
        get() = _navigationToPrivacyPolicy

    private var _navigationToUserAgreement = MutableLiveData<Boolean>()
    val navigationToUserAgreement: LiveData<Boolean>
        get() = _navigationToUserAgreement


    fun navigateToPrivacyPolicy() {
        _navigationToPrivacyPolicy.value = true
    }

    fun onNavigateToPrivacyPolicyFinish() {
        _navigationToPrivacyPolicy.value = false
    }


    fun navigateToUserAgreement() {
        _navigationToUserAgreement.value = true
    }

    fun onNavigateToUserAgreementFinish() {
        _navigationToUserAgreement.value = false
    }


    // login Fragment持有
    fun onLoginSuccessComplete() {
        _loginSuccessNavigation.value = false
    }

    // mainActivity检查登录状态
//    fun checkLoginStatus() {
//        userInfo.value = UserInfo("", "", "", "", 0L)
//        Timber.e("checkLoginStatus")
//        Timber.e("uid %s", LocalRepository.getUid())
//        if (LocalRepository.getUid().isEmpty()) {
//            //未登录，跳转到登录界面
//            _loginStatus.value = false
//        } else {
//            userInfo.value = LocalRepository.getUserInfo()
//        }
//    }

    fun initData() {
        account.value = LocalRepository.getAccount()
        password.value = ""
    }

    // mainActivity持有
//    fun onNavigateToLoginFinish() {
//        _loginStatus.value = true
//    }
}