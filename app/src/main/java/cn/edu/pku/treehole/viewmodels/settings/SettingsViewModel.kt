package cn.edu.pku.treehole.viewmodels.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.edu.pku.treehole.BuildConfig
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.base.network.ApiException
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.hole.HoleRepository
import cn.edu.pku.treehole.utilities.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

@HiltViewModel
class SettingsViewModel @Inject constructor(holeRepository: HoleRepository) :
    BaseViewModel(holeRepository = holeRepository) {

    private var _navigationToPrivacyPolicy = MutableLiveData<Boolean>()
    val navigationToPrivacyPolicy: LiveData<Boolean>
        get() = _navigationToPrivacyPolicy

    private var _navigationToUserAgreement = MutableLiveData<Boolean>()
    val navigationToUserAgreement: LiveData<Boolean>
        get() = _navigationToUserAgreement

    private var _navigationToCopyright = MutableLiveData<Boolean>()
    val navigationToCopyright: LiveData<Boolean>
        get() = _navigationToCopyright

    private var _navigationToAboutUs = MutableLiveData<Boolean>()
    val navigationToAboutUs: LiveData<Boolean>
        get() = _navigationToAboutUs

    private var _navigateToChangeTextSize = MutableLiveData<Boolean>()
    val navigateToChangeTextSize: LiveData<Boolean>
        get() = _navigateToChangeTextSize

    private var _navigateToChangeDarkModel = MutableLiveData<Boolean>()
    val navigateToChangeDarkModel: LiveData<Boolean>
        get() = _navigateToChangeDarkModel

    private var _navigateToSetQuoteComment = MutableLiveData<Boolean>()
    val navigateToSetQuoteComment: LiveData<Boolean>
        get() = _navigateToSetQuoteComment

    var currentDarkMode = MutableLiveData<Int?>().apply { value = LocalRepository.localDarkMode }

    var currentSetQuote = MutableLiveData<Boolean?>().apply { value = LocalRepository.localSetQuote }

    fun navigateToPrivacyPolicy(){
        _navigationToPrivacyPolicy.value = true
    }

    fun onNavigateToPrivacyPolicyFinish(){
        _navigationToPrivacyPolicy.value = false
    }

    fun navigateToChangeDarkModel(){
        _navigateToChangeDarkModel.value = true
    }

    fun onNavigateToChangeDarkModelFinish(){
        _navigateToChangeDarkModel.value = false
    }

    fun navigateToSetQuoteComment(){
        _navigateToSetQuoteComment.value = true
    }

    fun onNavigateToSetQuoteCommentFinish(){
        _navigateToSetQuoteComment.value = false
    }

    fun navigateToChangeTextSize(){
        _navigateToChangeTextSize.value = true
    }

    fun onNavigateToChangeTextSizeFinish(){
        _navigateToChangeTextSize.value = false
    }

    fun navigateToUserAgreement(){
        _navigationToUserAgreement.value = true
    }

    fun onNavigateToUserAgreementFinish(){
        _navigationToUserAgreement.value = false
    }

    fun navigateToCopyright(){
        _navigationToCopyright.value = true
    }

    fun onNavigateToCopyrightFinish(){
        _navigationToCopyright.value = false
    }

    fun navigateToAboutUs(){
        _navigationToAboutUs.value = true
    }

    fun onNavigateToAboutUsFinish(){
        _navigationToAboutUs.value = false
    }

    val canUpdate = SingleLiveData<Int>().apply { value = -1 }


    fun checkVersionUpdate() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadingStatus.postValue(true)
                val response =
                    database.checkUpdate()
                // 将数据存到本地
                LocalRepository.localUpdateInfo = response.data
                Timber.e("check update response %s", LocalRepository.localUpdateInfo!!.app_version_code)
                if((response.data?.app_version_code ?: 1) > BuildConfig.VERSION_CODE){
                    canUpdate.postValue(1)
                }else{
                    canUpdate.postValue(0)
                }
            } catch (e: Exception) {
                when (e) {
                    is ApiException -> failStatus.postValue(e)
                    else -> errorStatus.postValue(e)
                }
            } finally {
                loadingStatus.postValue(false)
            }
        }
    }

    fun clearCache(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                loadingStatus.postValue(true)
                database.clear()
                LocalRepository.isClearHoleCache = true
                LocalRepository.isClearAttentionCache = true
            }catch (e: Exception){
                when(e){
                    is ApiException -> handleHoleFailResponse(e)
                    else -> errorStatus.postValue(e)
                }
            }finally {
                Timer().schedule(1000) {
                    //...延时操作
                    loadingStatus.postValue(false)
                }

            }
        }
    }

    fun exitLogin(){
        viewModelScope.launch(Dispatchers.IO){
            database.clear()
            // 设置为未登录状态
            // 清楚localRepository部分数据
            LocalRepository.clearAll()
            _loginStatus.postValue(false)
        }
    }

}