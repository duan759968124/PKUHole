package cn.edu.pku.pkuhole.viewmodels.hole

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import cn.edu.pku.pkuhole.data.hole.HoleAllListDao
import cn.edu.pku.pkuhole.data.hole.HoleAllListItemBean

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/23
 * @Desc:
 * @Version:        1.0
 */
class HoleItemDetailViewModel(private val pid: Long = 0L, dataSource: HoleAllListDao) :
    ViewModel() {
    val database = dataSource

    val holeAllListItemBean: LiveData<HoleAllListItemBean> =
        database.getHoleDetailWithPid(pid).asLiveData()

//    fun getHoleItem() = holeAllListItemBean

}