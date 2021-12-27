package cn.edu.pku.pkuhole.viewmodels.hole

import android.app.Application
import androidx.lifecycle.*
import cn.edu.pku.pkuhole.data.hole.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HoleAllListViewModel @Inject internal constructor(
    holeAllListRepository: HoleAllListRepository
//    dataSource : HoleAllListDao, application: Application
) : ViewModel() {
    private val database = holeAllListRepository
    val holeAllList = database.getAllList().asLiveData()
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
        viewModelScope.launch {
//            val item = HoleAllListItemBean(
//                pid = 2925277,
//                hidden = 0,
//                text = "求推荐汉堡~想吃了",
//                type = "text",
//                timestamp = 1638325648,
//                reply = 0,
//                likenum = 1,
//                extra = 0,
//                url = "",
//                hot = 1638325648,
//                tag = ""
//            )
//            database.insert(item)
//            insertInitData()
            database.clear()
            val result : HoleApiResponse<List<HoleAllListItemBean>> = database.getHoleAllListFromNet(1)
            val holeGetList : List<HoleAllListItemBean>? = result.data
            if (holeGetList != null) {
                database.insertAll(holeGetList)
            }
//            Timber.e("fetch data result: %s", holeGetList.toString())
        }
    }

    private suspend fun insertInitData() {
        val item = HoleAllListItemBean(
            pid = 2925289,
            hidden = 0,
            text = "hhhhhh",
            type = "text",
            timestamp = 1638325656,
            reply = 0,
            likenum = 1,
            extra = 0,
            url = "",
            hot = 1638325648,
            tag = null
        )
        database.insert(item)
    }

}