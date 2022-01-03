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

    private var _replyDialogToName = MutableLiveData<String?>()

    val replyDialogToName: LiveData<String?>
        get() = _replyDialogToName

    private var _responseMsg = MutableLiveData<String?>()

    val responseMsg: LiveData<String?>
        get() = _responseMsg

//    var replyResponse = MutableLiveData<HoleApiResponse<Long>>()



    companion object {
        private const val HOLE_ITEM_DETAIL_PID = "pid"
    }

    init {

    }

    @SuppressLint("TimberExceptionLogging")
    fun fetchCommentDetailFromNet() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadingStatus.postValue(true)
                // Todo: 还需要添加一个验证有效期token的接口【获取token】
                database.getOneHoleFromNetToDatabase(pid)
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
        _replyDialogToName.value = ""
    }

    fun onCommentItemClicked(commentItem: CommentItemBean) {
        // 弹框
        _replyDialogToName.value = commentItem.name
    }


//    fun onCommentDialogFinished() {
//        // 弹框完成
//        _replyDialogToName.value = null
//    }

    // Todo:正常请求成功不显示，失败了才显示
    // Todo：如果成功非要显示，保证UI显示不冲突
    fun sendReplyComment(text: CharSequence) {
        Timber.e("viewModel reply text: %s", text)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Todo: 还需要添加一个验证有效期token的接口【获取token】
                loadingStatus.postValue(true)
//                errorStatus.postValue(null)
                val response = database.sendReplyComment(pid, text.toString())
                Timber.e("reply result %s", response.toString())
                loadingStatus.postValue(false)
                if(response.code == 0){
//                        _replyResponseMsg.postValue("回复成功")
                    response.data?.let {
                        Timber.e("reply data %s", response.data.toString())
                        fetchCommentDetailFromNet()
                    }
                }
                else {
//                        response.msg?.let { _replyResponseMsg.postValue(it) }
                }
            }catch (e: Exception){
                errorStatus.postValue(e)
                e.message?.let { Timber.e(e.message) }
            }finally {
                loadingStatus.postValue(false)
            }
        }
    }

    fun sendReportReason(text: CharSequence) {
        Timber.e("viewModel report text: %s", text)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Todo: 还需要添加一个验证有效期token的接口【获取token】
                _responseMsg.postValue(null)
                val response = database.report(pid, text.toString())
                Timber.e("reply result %s", response.toString())
                if(response.code == 0){
                    _responseMsg.postValue("举报成功")
                }else{
                    _responseMsg.postValue(response.msg)
                }
            }catch (e: Exception){
                errorStatus.postValue(e)
                e.message?.let { Timber.e(e.message) }
            }finally {
            }
        }
    }


    fun switchAttentionStatus() {
        // 发送请求，根据currentHoleItem中的isAttention来判断状态
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Todo: 还需要添加一个验证有效期token的接口【获取token】
                _responseMsg.postValue(null)
                if(currentHoleItem.value?.isAttention == null){
                    currentHoleItem.value?.isAttention = 0
                }
                currentHoleItem.value?.isAttention?.let { val response = database.switchAttentionStatus(
                    currentHoleItem.value!!,
                    kotlin.math.abs(it - 1))
                    if(response.code == 0){
                        _responseMsg.postValue("操作成功")
                    }else{
                        _responseMsg.postValue(response.msg)
                    }
                }
            }catch (e: Exception){
                errorStatus.postValue(e)
                e.message?.let { Timber.e(e.message) }
            }finally {
            }
        }
    }

//    fun onReplyResponseFinished() {
//        _replyResponse.value = null
//    }

}