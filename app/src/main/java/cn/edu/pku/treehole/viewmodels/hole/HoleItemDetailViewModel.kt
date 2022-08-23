package cn.edu.pku.treehole.viewmodels.hole

import android.annotation.SuppressLint
import androidx.lifecycle.*
import cn.edu.pku.treehole.adapters.HoleItemListener
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.base.network.ApiException
import cn.edu.pku.treehole.data.hole.CommentItemBean
import cn.edu.pku.treehole.data.hole.HoleRepository
import cn.edu.pku.treehole.utilities.HOLE_HOST_ADDRESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
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
@HiltViewModel
class HoleItemDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    holeRepository: HoleRepository) :
    BaseViewModel(holeRepository = holeRepository) {

    val pid : Long = savedStateHandle.get<Long>(HOLE_ITEM_DETAIL_PID)!!

    val currentHoleItem = database.getHoleItem(pid).asLiveData()

    val commentList: LiveData<List<CommentItemBean>> = database.getCommentList(pid).asLiveData()

    private var _replyDialogToName = MutableLiveData<String?>()
    val replyDialogToName: LiveData<String?>
        get() = _replyDialogToName

    private var _previewPicture = MutableLiveData(0L)

    val previewPicture: LiveData<Long>
        get() = _previewPicture

    private var _responseMsg = MutableLiveData<String?>()
    val responseMsg: LiveData<String?>
        get() = _responseMsg


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
                val token = getValidTokenWithFlow().singleOrNull()
                token?.let {
                    database.getOneHoleFromNetToDatabase(pid)
                    database.getCommentListFromNetToDatabase(pid)
                }

            }catch (e: Exception){
                when(e){
                    is ApiException -> handleHoleFailResponse(e)
                    else -> errorStatus.postValue(e)
                }
            }finally {
                loadingStatus.postValue(false)
            }
        }
    }

    val holeItemClickListener = HoleItemListener{
        _replyDialogToName.value = ""
    }

    val pictureClickListener = PictureClickListener{
        // 预览图片
        if (!it.url.isNullOrEmpty()) {
//            _previewPicture.value = HOLE_HOST_ADDRESS + "api/pku_image/" + it.pid
            _previewPicture.value = it.pid

        }

    }

    fun onCommentItemClicked(commentItem: CommentItemBean) {
        // 弹框
        _replyDialogToName.value = commentItem.name
    }


//    fun onCommentDialogFinished() {
//        // 弹框完成
//        _replyDialogToName.value = null
//    }

    fun sendReplyComment(text: CharSequence) {
        Timber.e("viewModel reply text: %s", text)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _responseMsg.postValue(null)
                val token = getValidTokenWithFlow().singleOrNull()
                token?.let {
                    val response = database.sendReplyComment(pid, text.toString())
                    Timber.e("reply result %s", response.toString())
                    _responseMsg.postValue("回复成功")
                    response.data?.let {
                        //重新获取树洞数据和评论数据
                        database.getOneHoleFromNetToDatabase(pid)
                        database.getCommentListFromNetToDatabase(pid)
                    }
                }
            }catch (e: Exception){
                when(e){
                    is ApiException -> handleHoleFailResponse(e)
                    else -> errorStatus.postValue(e)
                }
            }finally {
            }
        }
    }

    fun sendReportReason(text: CharSequence) {
        Timber.e("viewModel report text: %s", text)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _responseMsg.postValue(null)
                val token = getValidTokenWithFlow().singleOrNull()
                token?.let {
                    val response = database.report(pid, text.toString(), it)
                    Timber.e("reply result %s", response.toString())
                    _responseMsg.postValue("举报成功")
                }
            }catch (e: Exception){
                when(e){
                    is ApiException -> handleHoleFailResponse(e)
                    else -> errorStatus.postValue(e)
                }
            }finally {
            }
        }
    }

    fun switchAttentionStatus() {
        // 发送请求，根据currentHoleItem中的isAttention来判断状态
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _responseMsg.postValue(null)
                if(currentHoleItem.value?.is_follow == null){
                    currentHoleItem.value?.is_follow = 0
                }
                val token = getValidTokenWithFlow().singleOrNull()
                token?.let {
                    val response = database.switchAttentionStatus(
                        currentHoleItem.value!!,
                        kotlin.math.abs(currentHoleItem.value!!.is_follow!! - 1))
                        _responseMsg.postValue(response.data)
                }
            }catch (e: Exception){
                when(e){
                    is ApiException -> handleHoleFailResponse(e)
                    else -> errorStatus.postValue(e)
                }
            }finally {
            }
        }
    }

    fun finishPreviewPic() {
        _previewPicture.value = 0L
    }

//    fun onReplyResponseFinished() {
//        _replyResponse.value = null
//    }

}

