package cn.edu.pku.pkuhole.viewmodels.hole

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cn.edu.pku.pkuhole.data.hole.HoleAllListItemBean
import cn.edu.pku.pkuhole.data.hole.HoleAllListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HoleAllListViewModel @Inject internal constructor(
    holeAllListRepository: HoleAllListRepository
) : ViewModel() {
    val holeAllList = holeAllListRepository.getAllList().asLiveData()

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
            var item = HoleAllListItemBean(
                pid = 2925277,
                hidden = 0,
                text = "求推荐汉堡~想吃了",
                type = "text",
                timestamp = 1638325648,
                reply = 0,
                likenum = 1,
                extra = 0,
                url = "",
                hot = 1638325648,
                tag = ""
            )
            holeAllListRepository.insert(item)
            insertInitData()
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
        holeAllListRepository.insert(item)
    }

}