package cn.edu.pku.treehole.viewmodels.hole

import android.annotation.SuppressLint
import androidx.lifecycle.*
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.base.network.ApiException
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.hole.HoleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HoleListViewModel @Inject internal constructor(
    holeRepository: HoleRepository
) : BaseViewModel(holeRepository = holeRepository) {

    private val _navigationToHoleItemDetail = MutableLiveData<Long?>()
    val navigationToHoleItemDetail: MutableLiveData<Long?>
        get() = _navigationToHoleItemDetail

    private val _getRandomTipFromNet = MutableLiveData<Boolean>().apply { value = false }
    val getRandomTipFromNet: LiveData<Boolean>
        get() = _getRandomTipFromNet

    fun onHoleItemClicked(pid: Long) {
        _navigationToHoleItemDetail.value = pid
    }

    fun onHoleItemDetailNavigated() {
        _navigationToHoleItemDetail.value = null
    }

    fun closeRandomTipDialog() {
        _getRandomTipFromNet.value = false
    }

//    val holeList = database.getHoleList().asLiveData()
//    val commentListList = database.getCommentListList().asLiveData()

//    var firstComment = database.getFirstCommentByPid(4680226).asLiveData()
//    fun getFirstCommentByPid(pid: Long): LiveData<CommentItemBean?> {
//        firstComment = database.getFirstCommentByPid(pid).asLiveData()
//        return database.getFirstCommentByPid(pid).asLiveData()
//    }

    fun getFirstCommentByPid(pid: Long) = database.getFirstCommentByPid(pid).asLiveData()

    fun getSecondCommentByPid(pid: Long) = database.getSecondCommentByPid(pid).asLiveData()

    val holeInfoList = database.getHoleInfoList().asLiveData()

    var currentPage : Int = 1

//    val errorLiveData = MutableLiveData<Throwable>()


    init {
        Timber.e("hole list view model")
//        if(LocalRepository.getUid().isNotEmpty()){
//            // 获取一条随机的树洞管理规范
//            getRandomHoleManagementPractice()
//        }
//        currentPage = LocalRepository.localHoleListPage
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

    fun checkIsClearCache(){
        if(LocalRepository.isClearHoleCache){
            // 数据为空
            Timber.e("clear cache")
            currentPage = 1
            getHoleList()
            LocalRepository.isClearHoleCache = false
        }
    }

    private fun getRandomHoleManagementPractice() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val token = getValidTokenWithFlow().singleOrNull()
                token?.let {
                    val response =
                        database.getRandomHoleManagementPractice()
                    LocalRepository.localRandomTip = response.data?.desc ?: ""
                    Timber.e("response tip: %s", response.data?.desc)
                    Timber.e("localRandomTip tip: %s", LocalRepository.localRandomTip)
                    _getRandomTipFromNet.postValue(true)
                }
            } catch (e: Exception) {
                when (e) {
                    is ApiException -> handleHoleFailResponse(e)
                    else -> errorStatus.postValue(e)
                }
            } finally {
            }
        }

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
                    // 首次进入删除数据库并重新获取标签数据存放到数据库中
                    val token = getValidTokenWithFlow().singleOrNull()
                    token?.let {
                        database.getTagListFromNetToDatabase()
                    }
                }else{
                    loadingStatus.postValue(true)
                }
                val token = getValidTokenWithFlow().singleOrNull()
                Timber.e("valid token %s", token)
                token?.let { database.getHoleListFromNetToDatabase(currentPage) }
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
                token?.let {
                    database.refreshHoleListFromNetToDatabase() }
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