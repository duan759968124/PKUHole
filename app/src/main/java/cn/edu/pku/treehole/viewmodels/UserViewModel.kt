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


    private val _showInputValidCode = MutableLiveData<Boolean>().apply { value = false }
    val showInputValidCode: LiveData<Boolean>
        get() = _showInputValidCode

    private val _sendValidCodeSuccess = MutableLiveData<Boolean>().apply { value = false }
    val sendValidCodeSuccess: LiveData<Boolean>
        get() = _sendValidCodeSuccess

    private val _clickVerifyCode = MutableLiveData<Boolean>().apply { value = false }
    val clickVerifyCode: LiveData<Boolean>
        get() = _clickVerifyCode

    private val _verifySuccessNavigation = MutableLiveData<Boolean>().apply { value = false }
    val verifySuccessNavigation: LiveData<Boolean>
        get() = _verifySuccessNavigation


    private var isCompleteInput: Boolean = false
    var validCodeString: String = ""

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
//                    val userInfoRes = UserInfo(
//                        uid = response.uid!!,
//                        name = response.name,
//                        department = response.department,
//                        token = response.token,
//                        token_timestamp = response.token_timestamp
//                    )
                    // 数据存到本地
                    LocalRepository.localUserInfo = response.data
                    // todo: 后续需要去掉这句话
                    response.data?.let { LocalRepository.setUid(it.uid) }
                    LocalRepository.localJwtTimestamp = response.timestamp!!
                    LocalRepository.setAccount(account = account.value!!)
                    LocalRepository.setPasswordSecure(pwdSecure = passwordSecure.value!!)
                    // 用来更新activity中显示
                    userInfo.postValue(LocalRepository.localUserInfo)
                    _showInputValidCode.postValue(true)
//                    _loginSuccessNavigation.postValue(true)
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

    fun inputStatus(completeStatus: Boolean, code: String) {
        isCompleteInput = completeStatus
        validCodeString = code
    }

    fun finishInputIncompleteToast() {
        _clickVerifyCode.value = false
    }

    fun finishSendValidCode() {
        _sendValidCodeSuccess.value = false
    }


    fun getValidCode() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 发送手机验证码
                val responseSMSCode = database.sendSMSValidCode()
                Timber.e("send SMS Code %s", responseSMSCode)
                _sendValidCodeSuccess.postValue(responseSMSCode.success)
            } catch (e: Exception) {
                when (e) {
                    is ApiException -> failStatus.postValue(e)
                    else -> errorStatus.postValue(e)
                }
            } finally {
            }

        }
    }

    fun verifyValidCode() {
        Timber.e("validCodeString: $validCodeString")
        if (isCompleteInput) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    loadingStatus.postValue(true)
                    val response = database.verifySMSValidCode(valid_code = validCodeString)
                    Timber.e("verify response: %s", response)
                    LocalRepository.localUserInfo?.let { LocalRepository.setUid(it.uid) }
                    _verifySuccessNavigation.postValue(response.success)
                } catch (e: Exception) {
                    when (e) {
                        is ApiException -> failStatus.postValue(e)
                        else -> errorStatus.postValue(e)
                    }
                } finally {
                    loadingStatus.postValue(false)
                }
            }
        } else {
            // Toast出来
            _clickVerifyCode.value = true
        }
    }


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

    fun onNavigateToInputValidCodeComplete() {
        _showInputValidCode.value = false
    }

    fun onVerifySuccessComplete() {
        _verifySuccessNavigation.value = false
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