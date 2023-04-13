package cn.edu.pku.treehole.viewmodels.hole

import android.annotation.SuppressLint
import androidx.lifecycle.*
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.base.network.ApiException
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.hole.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.singleOrNull
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

//    val attentionList = database.getAttentionList().asLiveData()

    val attentionInfoList = database.getAttentionInfoList().asLiveData()

    var currentPage : Int = 1

    init {
        getAttentionList()
    }

    fun checkIsClearCache(){
        if(LocalRepository.isClearAttentionCache){
            // 数据为空
            Timber.e("check attention clear cache")
            currentPage = 1
            getAttentionList()
            LocalRepository.isClearAttentionCache = false
        }
    }

    // 封装toast请求，并显示在fragment中或者设置监听到BaseFragment中
    @SuppressLint("TimberExceptionLogging")
    fun getAttentionList(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 第一次是自动加载，显示refresh
                // 后续的请求都是加载更多数据导致的
                if(currentPage == 1){
                    refreshStatus.postValue(true)
                }else{
                    loadingStatus.postValue(true)
                }

                val token = getValidTokenWithFlow().singleOrNull()
//                token?.let {
//                    database.getAttentionListFromNetToDatabase() }
                token?.let { database.getAttentionListFromNetToDatabase(currentPage) }
                currentPage ++
                Timber.e("launch IO in first %d", currentPage)
            }catch (e: Exception){
                when(e){
                    is ApiException -> handleHoleFailResponse(e)
                    else -> errorStatus.postValue(e)
                }
            }finally {
                if(currentPage == 2){
                    refreshStatus.postValue(false)
                } else {
                    loadingStatus.postValue(false)
                }
                Timber.e("launch IO in second %d", currentPage)
            }
        }
    }

//    @SuppressLint("TimberExceptionLogging")
//    fun getAttentionList(){
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                refreshStatus.postValue(true)
//                val token = getValidTokenWithFlow().singleOrNull()
//                token?.let {
//                    database.getAttentionListFromNetToDatabase() }
//            }catch (e: Exception){
//                when(e){
//                    is ApiException -> handleHoleFailResponse(e)
//                    else -> errorStatus.postValue(e)
//                }
//            }finally {
//                refreshStatus.postValue(false)
//            }
//        }
//    }

    fun refreshAttentionList() {
        currentPage = 1
        getAttentionList()
    }

}