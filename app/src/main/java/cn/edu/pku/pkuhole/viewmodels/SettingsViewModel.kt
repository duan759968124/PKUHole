package cn.edu.pku.pkuhole.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.edu.pku.pkuhole.base.BaseViewModel
import cn.edu.pku.pkuhole.data.LocalRepository
import cn.edu.pku.pkuhole.data.hole.HoleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(holeRepository: HoleRepository) :
    BaseViewModel(holeRepository = holeRepository) {


    private var _navigationToCopyright = MutableLiveData<Boolean>()
    val navigationToCopyright: LiveData<Boolean>
        get() = _navigationToCopyright

    private var _navigationToAboutUs = MutableLiveData<Boolean>()
    val navigationToAboutUs: LiveData<Boolean>
        get() = _navigationToAboutUs

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
            // 清楚localRepository所有数据
            LocalRepository.clearAll()
            _loginStatus.postValue(false)
        }
    }

}