package cn.edu.pku.pkuhole.viewmodels.hole

import android.annotation.SuppressLint
import androidx.lifecycle.*
import cn.edu.pku.pkuhole.adapters.HoleItemListener
import cn.edu.pku.pkuhole.base.BaseViewModel
import cn.edu.pku.pkuhole.data.hole.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
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
    savedStateHandle: SavedStateHandle,
    holeRepository: HoleRepository) :
    BaseViewModel() {

    val pid : Long = savedStateHandle.get<Long>(HOLE_ITEM_DETAIL_PID)!!
    private val database = holeRepository

    val currentHoleItem = database.getHoleItem(pid).asLiveData()

    val commentList: LiveData<List<CommentItemBean>> = database.getCommentList(pid).asLiveData()

    companion object {
        private const val HOLE_ITEM_DETAIL_PID = "pid"
    }

    init {
        Timber.e("click hole pid: %s", pid)
    }

    @SuppressLint("TimberExceptionLogging")
    fun fetchCommentDetailFromNet() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadingStatus.postValue(true)
                // Todo: 还需要添加一个验证有效期token的接口【获取token】
//                database.getOneHoleFromNetToDatabase(pid)
                database.getCommentListFromNetToDatabase(pid)
            }catch (e: Exception){
                errorStatus.postValue(e)
                e.message?.let { Timber.e(e.message) }
            }finally {
                loadingStatus.postValue(false)
            }
        }
    }

    val holeItemClickListener = HoleItemListener{
        // Todo: 弹框
        Timber.e("pid %d", it)
    }

    fun onCommentItemClicked(commentItem: CommentItemBean) {
        // 弹框
        Timber.e("click comment %s", commentItem.toString())
    }

}