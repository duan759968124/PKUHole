package cn.edu.pku.pkuhole.viewmodels.hole

import android.annotation.SuppressLint
import androidx.lifecycle.*
import cn.edu.pku.pkuhole.api.HoleApiResponse
import cn.edu.pku.pkuhole.base.BaseViewModel
import cn.edu.pku.pkuhole.base.network.HoleApiException
import cn.edu.pku.pkuhole.base.network.StateLiveData
import cn.edu.pku.pkuhole.data.hole.*
import cn.edu.pku.pkuhole.utilities.ToastUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.lang.Exception
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class HoleListViewModel @Inject internal constructor(
    holeListRepository: HoleListRepository
) : BaseViewModel() {
    private val database = holeListRepository

    val mHoleListLiveData = StateLiveData<List<HoleListItemBean>>()

    val holeList = database.getHoleList().asLiveData()

//    val errorLiveData = MutableLiveData<Throwable>()

//    val holeAllList = database.getHoleAllListFromNet(1)
//    val holeAllList = holeAllListRepository.getAllList().asLiveData()

//    private val _navigationToHoleItemDetail = MutableLiveData<Long?>()
//
//    val navigationToHoleItemDetail: MutableLiveData<Long?>
//        get() = _navigationToHoleItemDetail

//    fun onHoleItemClicked(pid: Long) {
//        _navigationToHoleItemDetail.value = pid
//    }
//
//    fun onHoleItemDetailNavigated() {
//        _navigationToHoleItemDetail.value = null
//    }

    init {
        getHoleList()
//        Timber.e(mHoleListLiveData.value?.code.toString())
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

//    private suspend fun insertInitData() {

//        val item = HoleListItemBean(
//            pid = 2925289,
//            hidden = 0,
//            text = "hhhhhh",
//            type = "text",
//            timestamp = 1638325656,
//            reply = 0,
//            likenum = 1,
//            extra = 0,
//            url = "",
//            hot = 1638325648,
//            tag = null
//        )
//        holeListRepository.insert(item)
//    }

    // loading正常，但是异常抛出有问题
    // 封装toast请求，并显示在fragment中或者设置监听到BaseFragment中
    @SuppressLint("TimberExceptionLogging")
    fun getHoleList(){
        viewModelScope.launch(Dispatchers.IO) {
//            database.getHoleListFromNet(1, mHoleListLiveData)
            try {
                loadingLiveData.postValue(true)
                database.clear()
                // Todo: 还需要添加一个验证有效期token的接口【获取token】
                database.getHoleListFromNet(1)
            }catch (e: Exception){
//                val errorMsg = when(e){
//                    is UnknownHostException -> e.message
//                    else -> "None"
//                }
//                Timber.e(errorMsg.toString())
                errorLiveData.postValue(e)
                e.message?.let { Timber.e(e.message) }
            }finally {
                loadingLiveData.postValue(false)
            }
        }
    }

//    fun insertToDatabase(data: List<HoleListItemBean>){
//        viewModelScope.launch(Dispatchers.IO) {
//            database.clear()
//            database.insertAll(data)
//        }
//    }

}