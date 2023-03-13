package cn.edu.pku.treehole.viewmodels.hole

import android.annotation.SuppressLint
import androidx.lifecycle.*
import cn.edu.pku.treehole.adapters.HoleItemListener
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.base.network.ApiException
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.hole.CommentItemBean
import cn.edu.pku.treehole.data.hole.HoleItemBean
import cn.edu.pku.treehole.data.hole.HoleRepository
import cn.edu.pku.treehole.utilities.SingleLiveData
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

    private var _clickCommentId =  MutableLiveData(0L)

    companion object {
        private const val HOLE_ITEM_DETAIL_PID = "pid"
    }

//    var currentAscPage : Int = 1
//    var currentDescPage : Int = 1
    var currentPage : Int = 1
    fun fetchCommentDetailFromNetV2(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 第一次是自动加载，显示refresh
                // 后续的请求都是加载更多数据导致的
                if(currentPage == 1 && (_changeSortFlag.value?.rem(2) ?: 0) == 0){
                    // 首次进入的话删除掉sql数据库
//                    if(!_changeSortFlag.value!!){
//                        database.clearCommentByPId(pid = pid)
//                    }
                    refreshStatus.postValue(true)
                    // 首次进入删除数据库并重新获取标签数据存放到数据库中
                    val token = getValidTokenWithFlow().singleOrNull()
                    token?.let {
                        database.getOneHoleFromNetToDatabase(pid)
                    }
                }else{
                    loadingStatus.postValue(true)
                }
                val token = getValidTokenWithFlow().singleOrNull()
//                token?.let { database.getCommentListV3FromNetToDatabase(pid, page = if((_changeSortFlag.value?.rem(2) ?: 0) == 0) currentAscPage else currentDescPage, sort =if((_changeSortFlag.value?.rem(2) ?: 0) == 0)  "asc" else "desc") }
                token?.let { database.getCommentListV3FromNetToDatabase(pid, page =currentPage, sort =if((_changeSortFlag.value?.rem(2) ?: 0) == 0)  "asc" else "desc") }
//                if((_changeSortFlag.value?.rem(2) ?: 0) == 0) {
//                    currentAscPage++
//                } else {
//                    currentDescPage++
//                }
                currentPage ++
            }catch (e: Exception){
                when(e){
                    is ApiException -> handleHoleFailResponse(e)
                    else -> errorStatus.postValue(e)
                }
            }finally {
//                if((_changeSortFlag.value?.rem(2) ?: 0) == 0){
//                    if(currentAscPage == 2) {
//                        refreshStatus.postValue(false)
//                    } else {
//                        loadingStatus.postValue(false)
//                    }
//                }else{
//                    if(currentDescPage == 2) {
//                        refreshStatus.postValue(false)
//                    } else {
//                        loadingStatus.postValue(false)
//                    }
//                }
                if(currentPage == 2){
                    refreshStatus.postValue(false)
                }else{
                    loadingStatus.postValue(false)
                }
            }
        }
    }

    val holeItemClickListener = HoleItemListener{
        _replyDialogToName.value = ""
        _clickCommentId.value = 0L
    }

    val pictureClickListener = PictureClickListener{
        // 预览图片
        if (it.type == "image") {
//            _previewPicture.value = HOLE_HOST_ADDRESS + "api/pku_image/" + it.pid
            _previewPicture.value = it.pid

        }

    }

    private val _changeSortFlag: MutableLiveData<Int> by lazy{MutableLiveData(0)}
//    0 正序、全部 【逆序，只看洞主】
//    1 逆序、全部 【正序、只看洞主】
//    2 正序、只看洞主【逆序，还原】
//    3 逆序、只看洞主【正序、还原】
    var commentList = _changeSortFlag.switchMap { _allComment ->
        when(_allComment){
            0 -> {
                // _changeSortFlag没发生变化，commentList就不变化了，有问题，很大的问题，就不应该显示页码？
                database.getCommentList(pid).asLiveData()
            }
            1->{
                database.getCommentListDesc(pid).asLiveData()
            }
            2->{
                database.getCommentListOnlyLz(pid).asLiveData()
            }
            3->{
                database.getCommentListDescOnlyLz(pid).asLiveData()
            }
            else -> {
                database.getCommentList(pid).asLiveData()
            }
        }
    }
    val curCommentSort = _changeSortFlag.switchMap { _showText ->
        when(_showText){
            0, 2 -> MutableLiveData<String?>().apply { value = "逆序" }
            1, 3 -> MutableLiveData<String?>().apply { value = "正序" }
            else -> MutableLiveData<String?>().apply { value = "逆序" }
        }
    }


    val curCommentOnlyLz = _changeSortFlag.switchMap { _showText ->
        when(_showText){
            2, 3 -> MutableLiveData<String?>().apply { value = "还原" }
            else -> MutableLiveData<String?>().apply { value = "只看洞主" }
        }
    }

    fun onlyShowLzComment(){
        _changeSortFlag.value = (_changeSortFlag.value?.plus(2))?.rem(4)
    }

    fun changeCommentItemSort(){
        _changeSortFlag.value = ((_changeSortFlag.value?.rem(2)?.plus(1))?.rem(2) ?: 0) + ((_changeSortFlag.value?.div(2))?.times(2) ?: 0)
//       清理数据，设置正逆参数，
        viewModelScope.launch(Dispatchers.IO) {
            try {
                database.clearCommentByPId(pid)
//                if(_changeSortFlag.value == true && currentAscPage != 1){
//                    fetchCommentDetailFromNetV2()
//                }
//                if((_changeSortFlag.value == 1||_changeSortFlag.value == 3) && currentDescPage == 1){
//                    fetchCommentDetailFromNetV2()
//                }
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


    // 刷新数据
    @SuppressLint("TimberExceptionLogging")
    fun refreshCommentList() {
        //全变成0-1 全部【只看洞主】
        _changeSortFlag.value = _changeSortFlag.value?.div(2)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                refreshStatus.postValue(true)
                val token = getValidTokenWithFlow().singleOrNull()
                token?.let {
//                    database.refreshHoleListFromNetToDatabase()
                    database.getOneHoleFromNetToDatabase(pid)
//                    database.clearCommentByPId(pid)
                    if((_changeSortFlag.value?.rem(2) ?: 0) == 1){
                        currentPage = 1
                    }
                    if((_changeSortFlag.value?.rem(2) ?: 0) == 0){
                        currentPage --
                    }
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

    val loadMoreStatus = SingleLiveData<Boolean>()
    fun loadMoreData(){
        _changeSortFlag.value = _changeSortFlag.value?.div(2)
        if((currentPage-1)* 30 > (currentHoleItem.value?.reply ?: 0)) {
            Timber.e("没有更多数据")
            loadMoreStatus.value = false
        } else {
            Timber.e("需要加载下一页更多数据")
            loadMoreStatus.value = true
            fetchCommentDetailFromNetV2()
        }
//        if((_changeSortFlag.value?.rem(2) ?: 0) == 0){
//            if((currentPage-1)* 30 > (currentHoleItem.value?.reply ?: 0)) {
//                Timber.e("没有更多数据")
//                loadMoreStatus.value = false
//            } else {
//                Timber.e("需要加载下一页更多数据")
//                loadMoreStatus.value = true
//                fetchCommentDetailFromNetV2()
//            }
//        }else{
//            if((currentPage-1) * 30 > (currentHoleItem.value?.reply ?: 0)) {
//                Timber.e("没有更多数据")
//                loadMoreStatus.value = false
//            } else {
//                Timber.e("需要加载下一页更多数据")
//                loadMoreStatus.value = true
//                fetchCommentDetailFromNetV2()
//            }
//        }
    }


    fun onCommentItemClicked(commentItem: CommentItemBean) {
        // 弹框
        _replyDialogToName.value = commentItem.name
        _clickCommentId.value = commentItem.cid
    }


//    fun onCommentDialogFinished() {
//        // 弹框完成
//        _replyDialogToName.value = null
//    }

    fun sendReplyComment(text: CharSequence) {
        Timber.e("viewModel reply text: %s", text)
        Timber.e("viewModel reply clickCommentId: %d", _clickCommentId.value)
        _responseMsg.postValue(null)
        if(text == LocalRepository.lastReplyContent && _clickCommentId.value == LocalRepository.lastReplyCommentId && System.currentTimeMillis()-LocalRepository.lastReplyTimeStamp<10*1000){
            _responseMsg.postValue("请不要重复评论")
        }else{
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val token = getValidTokenWithFlow().singleOrNull()
                    token?.let {
//                    val response = database.sendReplyComment(pid, text.toString())
                        val response = database.sendReplyCommentV3(pid, text.toString(), commentId = if(_clickCommentId.value == 0L) null else _clickCommentId.value)
                        Timber.e("reply result %s", response.toString())
                        LocalRepository.lastReplyCommentId = _clickCommentId.value!!
                        LocalRepository.lastReplyContent = text.toString()
                        LocalRepository.lastReplyTimeStamp = System.currentTimeMillis()
                        _responseMsg.postValue("回复成功")
                        response.data?.let {
                            //重新获取树洞数据和评论数据
                            database.getOneHoleFromNetToDatabase(pid)
//                        database.getCommentListFromNetToDatabase(pid)
//                        database.getCommentListV3FromNetToDatabase(pid, page = 1, sort = "asc")
//                        database.clearCommentByPId(pid)
                            if((_changeSortFlag.value?.rem(2) ?: 0) == 1){
                                currentPage = 1
                            }else{
                                currentPage --
                            }
                            fetchCommentDetailFromNetV2()
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

