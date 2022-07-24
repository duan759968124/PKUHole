package cn.edu.pku.pkuhole.viewmodels

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.edu.pku.pkuhole.BuildConfig
import cn.edu.pku.pkuhole.base.BaseViewModel
import cn.edu.pku.pkuhole.base.network.ApiException
import cn.edu.pku.pkuhole.data.LocalRepository
import cn.edu.pku.pkuhole.data.hole.HoleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(holeRepository: HoleRepository) :
    BaseViewModel(holeRepository = holeRepository) {

    // -1 表示状态未知
    private val _canUpdate = MutableLiveData<Int>().apply { value = -1 }
    val canUpdate: LiveData<Int>
        get() = _canUpdate

    fun checkUpdate() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadingStatus.postValue(true)
                val response =
                    database.checkUpdate()
                // 将数据存到本地
                LocalRepository.localUpdateInfo = response.data
                Timber.e("check update response %s", LocalRepository.localUpdateInfo!!.app_version_code)
                if((response.data?.app_version_code ?: 1) > BuildConfig.VERSION_CODE){
                    _canUpdate.postValue(1)
                }else{
                    _canUpdate.postValue(0)
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

}