package cn.edu.pku.treehole.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.base.network.ApiException
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.hole.HoleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManMachineVerificationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    holeRepository: HoleRepository,
) : BaseViewModel(holeRepository = holeRepository) {

    companion object {
        private const val VERIFY_MASSAGE = "msg"
    }

    var hintMsg : String = savedStateHandle.get<String>(VERIFY_MASSAGE)!!

    var verificationCode = MutableLiveData<String>()


    private val _verifyCodeIsNull = MutableLiveData<Boolean>()
    val verifyCodeIsNull: LiveData<Boolean>
        get() = _verifyCodeIsNull

    private val _verifySuccessNavigation = MutableLiveData<Boolean>().apply { value = false }
    val verifySuccessNavigation: LiveData<Boolean>
        get() = _verifySuccessNavigation

    init {

    }


    fun reLogin() {
        viewModelScope.launch(Dispatchers.IO){
            database.clear()
            // 设置为未登录状态
            // 清楚localRepository部分数据
            _loginStatus.postValue(false)
            LocalRepository.clearAll()
        }
    }

    fun verifyOtpCode(){
        if(verificationCode.value.isNullOrEmpty()){
            _verifyCodeIsNull.value = true
        }else{
            _verifyCodeIsNull.value = false
            //请求
            viewModelScope.launch(Dispatchers.IO) {
                try {
//                    loadingStatus.postValue(true)
                    val token = getValidTokenWithFlow().singleOrNull()
                    token?.let { _ ->
                        val response = database.checkOtp(verificationCode.value!!)
                        if(response.success == true || response.msg == "success"){
                            _verifySuccessNavigation.postValue(true)
                        }
                    }
                }catch (e: Exception){
                    when(e){
                        is ApiException -> handleHoleFailResponse(e)
                        else -> errorStatus.postValue(e)
                    }
                }finally {
//                    loadingStatus.postValue(false)
                }
            }

        }
    }

    fun onVerifySuccessComplete() {
        _verifySuccessNavigation.value = false
    }
}