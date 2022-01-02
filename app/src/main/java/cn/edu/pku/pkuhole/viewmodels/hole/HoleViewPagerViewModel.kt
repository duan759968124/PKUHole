package cn.edu.pku.pkuhole.viewmodels.hole

import androidx.lifecycle.SavedStateHandle
import cn.edu.pku.pkuhole.base.BaseViewModel
import cn.edu.pku.pkuhole.data.hole.HoleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2022/1/2
 * @Desc:
 * @Version:        1.0
 */
@HiltViewModel
class HoleViewPagerViewModel @Inject constructor(
    holeRepository: HoleRepository) : BaseViewModel() {

    private val database = holeRepository

    // 发帖之后要涉及到更新hole数据和关注数据
    init {

    }

    fun onClickUploadFab(){
        Timber.e("start dialog post hole!!!")
    }
}