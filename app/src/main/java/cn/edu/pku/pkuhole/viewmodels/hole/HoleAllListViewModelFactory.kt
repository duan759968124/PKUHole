package cn.edu.pku.pkuhole.viewmodels.hole

import android.app.Application
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
class HoleAllListViewModelFactory(
    private val dataSource: HoleAllListDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HoleAllListViewModel::class.java)) {
            return HoleAllListViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}