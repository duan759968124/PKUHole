package cn.edu.pku.treehole.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.edu.pku.treehole.base.network.ApiException
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.hole.HoleRepository
import cn.edu.pku.treehole.utilities.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/28
 * @Desc:
 * @Version:        1.0
 */
@HiltViewModel
open class BaseViewModel @Inject internal constructor(
    holeRepository: HoleRepository
): ViewModel() {
    // 加载更多和请求加载等待状态
    val loadingStatus = MutableLiveData<Boolean>()

    // 刷新等待状态
    val refreshStatus = MutableLiveData<Boolean>()
    // error 变量
//    val errorStatus = MutableLiveData<Throwable>()
//    网络异常错误监听【只需要toast的异常】
    val errorStatus = SingleLiveData<Throwable>()
//    API错误监听【需要退出到登陆的异常】
    val failStatus = SingleLiveData<ApiException>()
    val database  = holeRepository

//    token 请求前有效的token
//    protected val _validToken = MutableLiveData<String>()
//    val validToken: LiveData<String>
//        get() = _validToken

//  未登录状态，fragment监听后处理：跳转到登录界面
    protected val _loginStatus = MutableLiveData<Boolean>()
        val loginStatus: LiveData<Boolean>
            get() = _loginStatus


//   获取token相当于后台登录，如果后台登录失败，则跳转到前台
//    fun getValidToken(){
//        _validToken.value = ""
//        if(LocalRepository.getValidToken().isNotEmpty()){
//            _validToken.value = LocalRepository.getValidToken()
//        }else{
//            viewModelScope.launch(Dispatchers.IO){
//                try {
//                    val response =
//                        database.login(account = LocalRepository.getAccount(), password = LocalRepository.getPassword())
//                    Timber.e("login response %s", response)
//                    val userInfoRes = UserInfo(
//                        uid = response.uid!!,
//                        name = response.name,
//                        department = response.department,
//                        token = response.token,
//                        token_timestamp = response.token_timestamp
//                    )
//                    // 将数据存到本地
//                    LocalRepository.setUserInfo(userInfoRes)
//                    response.token?.let{ _validToken.value = it }
//                }catch (e : Exception){
//                    when(e){
//                        is ApiException -> handleTokenFailResponse(e as ApiException)
//                        else -> errorStatus.postValue(e)
//                    }
//                }finally {
//                }
//            }
//        }
//    }


    fun getValidTokenWithFlow(): Flow<String?> = flow {
        val token = LocalRepository.getValidToken()
        if (token.isNotEmpty()) {
            emit(token)
        } else {
            val response = database.loginSecure(
                account = LocalRepository.getAccount(),
                passwordSecure = LocalRepository.getPasswordSecure()
            )
//            val userInfoRes = UserInfo(
//                uid = response.uid!!,
//                name = response.name,
//                department = response.department,
//                token = response.token,
//                token_timestamp = response.token_timestamp
//            )
            // 将数据存到本地
//            LocalRepository.setUserInfo(userInfoRes)
//            emit(response.token)
            // 数据存到本地
            LocalRepository.localUserInfo = response.data
            response.data?.let { LocalRepository.setUid(it.uid) }
            LocalRepository.localJwtTimestamp = response.timestamp!!
            response.data?.let { emit(it.jwt) }
        }
    }
    .flowOn(Dispatchers.IO)
        .catch { exception ->
            run {
                when (exception) {
                    is ApiException -> handleTokenFailResponse(exception)
                    else -> errorStatus.postValue(exception)
                }
            }
        }

    private fun handleTokenFailResponse(apiException: ApiException) {
        Timber.e("exec handleTokenFailResponse %d %s", apiException.code, apiException.msg)
        when(apiException.code){
            // 错误请求code 1 2 4 100
            1 -> clearDataAndReLogin(apiException)
            2 -> clearDataAndReLogin(apiException)
            4 -> clearDataAndReLogin(apiException)
            100 -> clearDataAndReLogin(apiException)
            401 -> clearDataAndReLogin(apiException)
            // 其他fragment需要监听failStatus状态变化，并toast出来
            else -> failStatus.postValue(apiException)
        }
    }


    fun handleHoleFailResponse(apiException: ApiException) {
        Timber.e("exec handleHoleFailResponse %d %s", apiException.code, apiException.msg)
        when (apiException.code) {
            // 错误请求code 2 [会话无效，请重新登录]
//            2 -> clearDataAndReLogin(apiException)
            401 -> clearDataAndReLogin(apiException)
            40002 -> {
                apiException.msg = "请重新登录并进行手机短信验证"
                clearDataAndReLogin(apiException)
            }
            // 其他fragment需要监听failStatus状态变化，并toast出来
            else -> failStatus.postValue(apiException)
        }
    }


    private fun clearDataAndReLogin(apiException: ApiException) {
        viewModelScope.launch(Dispatchers.IO){
            database.clear()
            // 设置为未登录状态
            // 清楚localRepository部分数据
            LocalRepository.clearAll()
            _loginStatus.postValue(false)
            failStatus.postValue(apiException)
        }

    }

    // fragment执行跳转之后，将_loginStatus变为true
    fun onNavigateToLoginFinish() {
        _loginStatus.value = true
    }


}