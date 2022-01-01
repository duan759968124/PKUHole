package cn.edu.pku.pkuhole.viewmodels.hole

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import cn.edu.pku.pkuhole.data.hole.HoleListItemBean
import cn.edu.pku.pkuhole.data.hole.HoleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/23
 * @Desc:
 * @Version:        1.0
 */
//class HoleItemDetailViewModel(private val pid: Long = 0L, dataSource: HoleAllListDao) :
// Todo：修改仓库为hole Detail的仓库
@HiltViewModel
class HoleItemDetailViewModel @Inject constructor(
//    private val pid: Long = 0L,
    savedStateHandle: SavedStateHandle,
    holeRepository: HoleRepository) :
    ViewModel() {
    val pid : Long = savedStateHandle.get<Long>(HOLE_ITEM_DETAIL_PID)!!
    val holeListItemBean: LiveData<HoleListItemBean> =
        holeRepository.getHoleDetailWithPid(pid).asLiveData()

//    fun getHoleItem() = holeAllListItemBean
    companion object {
        private const val HOLE_ITEM_DETAIL_PID = "pid"
    }

    init {
        Timber.e("click hole pid: %s", pid)
    }

}