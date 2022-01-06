package cn.edu.pku.pkuhole.viewmodels.hole

import android.annotation.SuppressLint
import androidx.lifecycle.*
import cn.edu.pku.pkuhole.base.BaseViewModel
import cn.edu.pku.pkuhole.data.hole.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AttentionViewModel @Inject internal constructor(
    holeRepository: HoleRepository
) : BaseViewModel(holeRepository = holeRepository) {
//    private val database = holeRepository

    private val _navigationToHoleItemDetail = MutableLiveData<Long?>()
    val navigationToHoleItemDetail: MutableLiveData<Long?>
        get() = _navigationToHoleItemDetail
    fun onHoleItemClicked(pid: Long) {
        _navigationToHoleItemDetail.value = pid
    }
    fun onHoleItemDetailNavigated() {
        _navigationToHoleItemDetail.value = null
    }

    val attentionList = database.getAttentionList().asLiveData()

    init {
//        getAttentionList()
    }

    // 封装toast请求，并显示在fragment中或者设置监听到BaseFragment中
    @SuppressLint("TimberExceptionLogging")
    fun getAttentionList(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Todo: 还需要添加一个验证有效期token的接口【获取token】
                refreshStatus.postValue(true)
//                database.clearAttentionList()
                database.getAttentionListFromNetToDatabase()
            }catch (e: Exception){
                errorStatus.postValue(e)
                e.message?.let { Timber.e(e.message) }
            }finally {
                refreshStatus.postValue(false)
            }
        }
    }

    fun refreshAttentionList() {
        getAttentionList()
    }

}