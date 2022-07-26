package cn.edu.pku.treehole.viewmodels.hole

import android.annotation.SuppressLint
import androidx.lifecycle.*
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.base.network.ApiException
import cn.edu.pku.treehole.data.hole.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HoleListViewModel @Inject internal constructor(
    holeRepository: HoleRepository
) : BaseViewModel(holeRepository = holeRepository) {

    private val _navigationToHoleItemDetail = MutableLiveData<Long?>()

    val navigationToHoleItemDetail: MutableLiveData<Long?>
        get() = _navigationToHoleItemDetail

    fun onHoleItemClicked(pid: Long) {
        _navigationToHoleItemDetail.value = pid
    }

    fun onHoleItemDetailNavigated() {
        _navigationToHoleItemDetail.value = null
    }

    val holeList = database.getHoleList().asLiveData()

    var currentPage : Int = 1

//    val errorLiveData = MutableLiveData<Throwable>()



    init {
        getHoleList()

//        Timber.e("valid token %s", token.value)
//        launch(
//            {
//                loadingLiveData.postValue(true)
//                holeListRepository.clear()
//                holeListRepository.getHoleListFromNet(1)
//            },
//            { errorLiveData.postValue(it) },
//            { loadingLiveData.postValue(false) }
//        )
//        viewModelScope.launch {
//            try{
//                holeListRepository.clear()
//                holeListRepository.addHoleListPageToDatabase(1)
//            }catch (e: Exception){
//                errorLiveData.postValue(e)
//                Timber.e(e.toString())
////                error(e)
//            }finally {
//
//            }
////            val result : HoleApiResponse<List<HoleListItemBean>> = database.getHoleAllListFromNet(1)
////            val holeListGetList : List<HoleListItemBean>? = result.data
////            if (holeListGetList != null) {
////                database.insertAll(holeListGetList)
////            }
////            Timber.e("fetch data result: %s", holeGetList.toString())
//        }
    }

    // 封装toast请求，并显示在fragment中或者设置监听到BaseFragment中
    // 按页加载数据
    @SuppressLint("TimberExceptionLogging")
    fun getHoleList(){
        Timber.e("launch IO out first %d", currentPage)
        viewModelScope.launch(Dispatchers.IO) {
//            database.getHoleListFromNet(1, mHoleListLiveData)
            try {
                // 第一次是自动加载，显示refresh
                // 后续的请求都是加载更多数据导致的
                if(currentPage == 1){
                    // 首次进入的话删除掉sql数据库
                    database.clear()
                    refreshStatus.postValue(true)
                }else{
                    loadingStatus.postValue(true)
                }
                val token = getValidTokenWithFlow().singleOrNull()
                Timber.e("valid token %s", token)
                token?.let { database.getHoleListFromNetToDatabase(currentPage, it) }
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
        Timber.e("launch IO out second %d", currentPage)
    }

    // 刷新数据
    @SuppressLint("TimberExceptionLogging")
    fun refreshHoleList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                refreshStatus.postValue(true)
                val token = getValidTokenWithFlow().singleOrNull()
                token?.let { database.refreshHoleListFromNetToDatabase(it) }
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

}