package cn.edu.pku.treehole.viewmodels.hole

import android.annotation.SuppressLint
import androidx.lifecycle.*
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.base.network.ApiException
import cn.edu.pku.treehole.data.hole.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
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
                refreshStatus.postValue(true)
                val token = getValidTokenWithFlow().singleOrNull()
                token?.let { database.getAttentionListFromNetToDatabase(it) }
            }catch (e: Exception){
                when(e){
                    is ApiException -> handleHoleFailResponse(e)
                    else -> errorStatus.postValue(e)
                }
            }finally {
                refreshStatus.postValue(false)
            }
        }
    }

    fun refreshAttentionList() {
        getAttentionList()
    }

}