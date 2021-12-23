package cn.edu.pku.pkuhole.viewmodels.hole

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.edu.pku.pkuhole.data.hole.HoleAllListDao
import cn.edu.pku.pkuhole.data.hole.HoleAllListItemBean
import kotlinx.coroutines.launch

class HoleAllListViewModel(dataSource: HoleAllListDao, application: Application) : ViewModel() {
    val database = dataSource
    val holeAllList = database.getAllList()

    private val _navigationToHoleItemDetail = MutableLiveData<Long?>()

    val navigationToHoleItemDetail: MutableLiveData<Long?>
        get() = _navigationToHoleItemDetail

    fun onHoleItemClicked(pid: Long) {
        _navigationToHoleItemDetail.value = pid
    }

    fun onHoleItemDetailNavigated() {
        _navigationToHoleItemDetail.value = null
    }

    init {
        viewModelScope.launch {
            insertInitData()
        }
    }

    private suspend fun insertInitData() {
        var item = HoleAllListItemBean(
            pid = 2925277,
            hidden = 0,
            text = "求推荐汉堡~想吃了",
            type = "text",
            timestamp = 1638325648,
            replyNum = 0,
            likeNum = 1,
            extra = 0,
            url = "",
            hot = 1638325648,
            tag = ""
        )
        database.insert(item)
        item = HoleAllListItemBean(
            pid = 2925289,
            hidden = 0,
            text = "hhhhhh",
            type = "text",
            timestamp = 1638325656,
            replyNum = 0,
            likeNum = 1,
            extra = 0,
            url = "",
            hot = 1638325648,
            tag = null
        )
        database.insert(item)
    }

}