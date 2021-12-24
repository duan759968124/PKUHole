package cn.edu.pku.pkuhole.viewmodels.hole

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.edu.pku.pkuhole.data.hole.HoleAllListDao

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/23
 * @Desc:
 * @Version:        1.0
 */
class HoleItemDetailViewModelFactory(
    private val pid: Long,
    private val dataSource: HoleAllListDao,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HoleItemDetailViewModel::class.java)) {
            return HoleItemDetailViewModel(pid, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}