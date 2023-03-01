package cn.edu.pku.treehole.viewmodels.hole

import android.annotation.SuppressLint
import androidx.lifecycle.*
import cn.edu.pku.treehole.adapters.HoleItemListener
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.base.network.ApiException
import cn.edu.pku.treehole.data.LocalRepository
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

//    var commentList: LiveData<List<CommentItemBean>> = database.getCommentList(pid).asLiveData()



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


    var currentPage : Int = 1
    fun fetchCommentDetailFromNetV2(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 第一次是自动加载，显示refresh
                // 后续的请求都是加载更多数据导致的
                if(currentPage == 1){
                    // 首次进入的话删除掉sql数据库
                    database.clearCommentByPId(pid = pid)
                    refreshStatus.postValue(true)
                    // 首次进入删除数据库并重新获取标签数据存放到数据库中
                    val token = getValidTokenWithFlow().singleOrNull()
                    token?.let {
                        database.getOneHoleFromNetToDatabase(pid)
                        database.getCommentListV3FromNetToDatabase(pid, page = currentPage, sort = if(_changeSortFlag.value == true)  "asc" else "desc")
                    }
                }else{
                    loadingStatus.postValue(true)
                }
                val token = getValidTokenWithFlow().singleOrNull()
                token?.let { database.getCommentListV3FromNetToDatabase(pid, page = currentPage, sort =if(_changeSortFlag.value == true)  "asc" else "desc") }
                currentPage ++
            }catch (e: Exception){
                when(e){
                    is ApiException -> handleHoleFailResponse(e)
                    else -> errorStatus.postValue(e)
                }
            }finally {
                if(currentPage == 2){
                    refreshStatus.postValue(false)
                } else {
                    loadingStatus.postValue(false)
                }
            }
        }
    }

    @SuppressLint("TimberExceptionLogging")
    fun fetchCommentDetailFromNet() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                loadingStatus.postValue(true)
                val token = getValidTokenWithFlow().singleOrNull()
                token?.let {
                    database.getOneHoleFromNetToDatabase(pid)
//                    database.getCommentListFromNetToDatabase(pid)
                    database.getCommentListV3FromNetToDatabase(pid, page = 1, sort = "asc")
                }

            }catch (e: Exception){
                when(e){
                    is ApiException -> handleHoleFailResponse(e)
                    else -> errorStatus.postValue(e)
                }
            }finally {
//                loadingStatus.postValue(false)
            }
        }
    }

    val holeItemClickListener = HoleItemListener{
        _replyDialogToName.value = ""
    }

    val pictureClickListener = PictureClickListener{
        // 预览图片
        if (it.type == "image") {
//            _previewPicture.value = HOLE_HOST_ADDRESS + "api/pku_image/" + it.pid
            _previewPicture.value = it.pid

        }

    }

//    var curCommentSort = MutableLiveData<String?>().apply { value = "逆序" }
    private val _changeSortFlag: MutableLiveData<Boolean> by lazy{MutableLiveData(true)}
    val commentList =_changeSortFlag.switchMap { _allComment ->
        when(_allComment){
            true -> {
                database.getCommentList(pid).asLiveData()
            }
            else -> {
                database.getCommentListDesc(pid).asLiveData()
            }
        }
    }
    val curCommentSort = _changeSortFlag.switchMap { _showText ->
        when(_showText){
            true -> MutableLiveData<String?>().apply { value = "逆序" }
            else -> MutableLiveData<String?>().apply { value = "正序" }
        }
    }

    fun changeCommentItemSort(){
        _changeSortFlag.value = _changeSortFlag.value != true
//       清理数据，设置正逆参数，
        viewModelScope.launch(Dispatchers.IO) {
            try {
                database.clearCommentByPId(pid)
                currentPage = 1
                fetchCommentDetailFromNetV2()
//                database.getCommentListV3FromNetToDatabase(pid, 1, sort = if(_changeSortFlag.value == true)  "asc" else "desc")
            }catch (e: Exception){
                when(e){
                    is ApiException -> handleHoleFailResponse(e)
                    else -> errorStatus.postValue(e)
                }
            } finally {

            }

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
//                        database.getCommentListFromNetToDatabase(pid)
                        database.getCommentListV3FromNetToDatabase(pid, page = 1, sort = "asc")
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

    // 刷新数据
    @SuppressLint("TimberExceptionLogging")
    fun refreshCommentList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                refreshStatus.postValue(true)
                val token = getValidTokenWithFlow().singleOrNull()
                token?.let {
//                    database.refreshHoleListFromNetToDatabase()
                    database.getOneHoleFromNetToDatabase(pid)
                    database.clearCommentByPId(pid)
                    currentPage = 1
                    fetchCommentDetailFromNetV2()
                }
            }catch (e: Exception){
                when(e){
                    is ApiException -> handleHoleFailResponse(e)
                    else -> errorStatus.postValue(e)
                }
            }finally {
                refreshStatus.postValue(false)
            }
        }
    }

}

