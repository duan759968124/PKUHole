package cn.edu.pku.treehole.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.hole.HoleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

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


    fun navigateToPrivacyPolicy(){
        _navigationToPrivacyPolicy.value = true
    }

    fun onNavigateToPrivacyPolicyFinish(){
        _navigationToPrivacyPolicy.value = false
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