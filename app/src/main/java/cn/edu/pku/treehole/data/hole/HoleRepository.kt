package cn.edu.pku.treehole.data.hole

import cn.edu.pku.treehole.api.HoleApiResponse
import cn.edu.pku.treehole.api.HoleApiService
import cn.edu.pku.treehole.base.BaseRepository
import cn.edu.pku.treehole.data.EmptyBean
import cn.edu.pku.treehole.data.HoleManagementPracticeBean
import cn.edu.pku.treehole.data.UpdateInfo
import cn.edu.pku.treehole.data.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 * @Time:           2021/12/24
 * @Desc:
 * @Version:        1.0
 */

@Singleton
class HoleRepository @Inject constructor(
    private val holeListDao: HoleListDao,
    private val commentDao: CommentDao,
    private val tagDao: TagDao,
    private val holeApi: HoleApiService,
) : BaseRepository() {

    fun getHoleList(): Flow<List<HoleItemBean>> = holeListDao.getHoleList()

    fun getAttentionList(): Flow<List<HoleItemBean>> = holeListDao.getAttentionList()

    private suspend fun updateOrInsertHoleList(holeListList: List<HoleListItemBean>) =
        holeListDao.upsertHoleList(holeListList)

    private suspend fun updateOrInsertAttentionList(attentionList: List<AttentionItemBean>) =
        holeListDao.upsertAttentionList(attentionList)


    // 记录获取第一页或者刷新时间，作为请求刷新的一个参数
    private var getFirstPageOrRefreshHoleListTimestamp: Long = 1660289350

    suspend fun getHoleListFromNetToDatabase(page: Int) {
        withContext(Dispatchers.IO) {
            val holeListResponse = launchRequest { holeApi.getHoleList(page = page, limit = 30) }
            holeListResponse.data!!.data?.map {
                it.isHole = 1
                it.isRead = 1
//                if(!it.url.isNullOrEmpty()){
//                    val pictureResponse =  launchRequestPic { holeApi.getPictureFromPid(it.pid)}
//                    if(pictureResponse.code == 20000){
//                        it.pic_data = pictureResponse.data
//                    }
//                }
                if (it.reply > 0) {
                    //存在评论
                    val holeResponse = launchRequest { holeApi.getCommentList(pid = it.pid) }
                    val randomH = Math.random()
                    holeResponse.data?.data?.map { commentItem ->
                        commentItem.randomH = randomH
                    }
                    holeResponse.data?.data.let { listCommentItem ->
                        if (listCommentItem != null) {
                            insertCommentList(listCommentItem)
                        }
                    }
                }
            }
            holeListResponse.data.data?.let { updateOrInsertHoleList(it) }
            if (page == 1) {
                holeListResponse.timestamp?.let { getFirstPageOrRefreshHoleListTimestamp = it }
                //测试小黄点
//                getFirstPageOrRefreshHoleListTimestamp =
//                    holeListResponse.data.data?.get(5)!!.timestamp
            }
        }
    }

    suspend fun refreshHoleListFromNetToDatabase() {
        withContext(Dispatchers.IO) {
//            Timber.e("getFirstPageOrRefreshHoleListTimestamp: $getFirstPageOrRefreshHoleListTimestamp")
            val refreshHoleListResponse =
                launchRequest { holeApi.refreshHoleList(timestamp = getFirstPageOrRefreshHoleListTimestamp) }
            refreshHoleListResponse.data?.map {
                it.isHole = 1
                it.isRead = 0
                if (it.reply > 0) {
                    val holeResponse = launchRequest { holeApi.getCommentList(pid = it.pid) }
                    val randomH = Math.random()
                    holeResponse.data?.data?.map { commentItem ->
                        commentItem.randomH = randomH
                    }
                    holeResponse.data?.data.let { listCommentItem ->
                        if (listCommentItem != null) {
                            insertCommentList(listCommentItem)
                        }
                    }
                }
            }
            refreshHoleListResponse.data?.let { updateOrInsertHoleList(it) }
            refreshHoleListResponse.timestamp?.let { getFirstPageOrRefreshHoleListTimestamp = it }

        }
    }

    // 获取attention列表  更换表格insert另一个表中
    suspend fun getAttentionListFromNetToDatabase() {
        withContext(Dispatchers.IO) {
            val attentionListResponse = launchRequest { holeApi.getAttentionList() }
//            attentionListResponse.data?.map { it.is_follow = 1 }
            attentionListResponse.data!!.data?.map {
//                if(!it.url.isNullOrEmpty()){
//                    val pictureResponse =  launchRequestPic { holeApi.getPictureFromPid(it.pid)}
//                    if(pictureResponse.code == 20000){
//                        it.pic_data = pictureResponse.data
//                    }
//                }
                if (it.reply > 0) {
                    //存在评论
                    val holeResponse = launchRequest { holeApi.getCommentList(pid = it.pid) }
                    val randomH = Math.random()
                    holeResponse.data?.data?.map { commentItem ->
                        commentItem.randomH = randomH
                    }
                    holeResponse.data?.data.let { listCommentItem ->
                        if (listCommentItem != null) {
                            insertCommentList(listCommentItem)
                        }
                    }
                }
            }
            attentionListResponse.data.data.let {
                if (it != null) {
                    updateOrInsertAttentionList(it)
                }
            }
        }
    }

    // 树洞详情相关函数
    // update holeItemBean函数
    fun getHoleItem(pid: Long) = holeListDao.get(pid)
//    fun getHoleItem(pid: Long) = holeListDao.get(pid).map { it?.asDomainModel() }

    private suspend fun updateOrInsertHoleItemModel(holeItemModel: HoleItemModel) =
        holeListDao.upsertModel(holeItemModel)


    fun getCommentList(pid: Long) = commentDao.getCommentListByPid(pid)

    fun getCommentListDesc(pid: Long) = commentDao.getCommentListByPidDesc(pid)

    fun getFirstCommentByPid(pid: Long) = commentDao.getFirstCommentByPid(pid)

    fun getSecondCommentByPid(pid: Long) = commentDao.getSecondCommentByPid(pid)

    fun getCommentListList(): Flow<List<List<CommentItemBean>>> =
        getHoleList()
            .onEach { Timber.e("hole list size: ${it.size}") }
            .map { list -> list.map { holeItemBean -> commentDao.getCommentListByPidLimit2(holeItemBean.pid) } }
            .map { flows -> combine(flows) {
                it.toList() } }
            .flattenMerge()

    fun getHoleInfoList(): Flow<List<HoleInfoBean>> = getHoleList()
            .onEach { Timber.e("hole list size: ${it.size}") }
            .map { list ->
                list.map { holeItemBean -> HoleInfoBean(
                    holeItemBean.pid,
                    holeItemBean,
                    getFirstCommentByPid(holeItemBean.pid).first(),
                    getSecondCommentByPid(holeItemBean.pid).first()
                ) } }

    fun getAttentionInfoList(): Flow<List<HoleInfoBean>> = getAttentionList()
        .onEach { Timber.e("attention list size: ${it.size}") }
        .map { list ->
            list.map { holeItemBean -> HoleInfoBean(
                holeItemBean.pid,
                holeItemBean,
                getFirstCommentByPid(holeItemBean.pid).first(),
                getSecondCommentByPid(holeItemBean.pid).first()
            ) } }


    fun getHoleInfoBeanList2(): Flow<ArrayList<HoleInfoBean>> {
//        return getHoleList().map {
//            val holeInfoList = ArrayList<HoleInfoBean>()
//            it.forEach { holeItemBean ->
////                getCommentList(holeItemBean.pid).collect{ commentList ->
////                    Timber.e("commentList: $commentList")
////                }
//                holeInfoList.add(HoleInfoBean(holeItemBean, null))
//            }
//           holeInfoList
//        }

//        return getHoleList().map { holeItemList ->
//            holeItemList.forEach {
//                it
//            }
//        }.map { holeItemBean ->
//            Timber.e("holeItemBean $holeItemBean")
//            val holeInfoList = ArrayList<HoleInfoBean>()
////            holeInfoList.add(HoleInfoBean(, null))
//            holeInfoList
//        }
        val holeInfoList = ArrayList<HoleInfoBean>()
        val result = getHoleList().flatMapMerge {
            it.asFlow()
        }.flatMapLatest {
            holeInfoList.add(HoleInfoBean(it.pid, it, getFirstCommentByPid(it.pid).first(), getSecondCommentByPid(it.pid).first()))
            Timber.e("HoleInfoBean size 2: ${holeInfoList.size}")
            flowOf(holeInfoList)
        }
        return result
    }

    private suspend fun insertCommentList(commentList: List<CommentItemBean>) =
        commentDao.insertCommentList(commentList)

    // 获取一条树洞详情,【这条数据如果出现在树洞列表或者关注列表中，一定存在数据库中，并且isHole或者isAttention一定存在，所以直接更新】并更新数据库
    // 如果是搜索列表中的数据，则不一定存在。点击搜索列表结果请求，要把这条数据插入到数据库中，isHole或者isAttention可能存在可能不存在，需要插入或者更新。
    suspend fun getOneHoleFromNetToDatabase(pid: Long) {
        withContext(Dispatchers.IO) {
            val holeResponse = launchRequest { holeApi.getOneHole(pid = pid) }
//            holeResponse.data?.let { it.reply = 100 }
            holeResponse.data?.let {
                it.isRead = 1
//                if(!it.url.isNullOrEmpty()){
//                    val pictureResponse =  launchRequestPic { holeApi.getPictureFromPid(it.pid)}
//                    if(pictureResponse.code == 20000){
//                        it.pic_data = pictureResponse.data
//                    }
//                }
            }
            holeResponse.data?.let { updateOrInsertHoleItemModel(it) }
        }
    }

    // 获取评论列表，并插入到数据库中【这里不用更新】
    suspend fun getCommentListFromNetToDatabase(pid: Long) {
        withContext(Dispatchers.IO) {
            val holeResponse = launchRequest { holeApi.getCommentList(pid = pid) }
            val randomH = Math.random()
            holeResponse.data?.data?.map {
                it.randomH = randomH
            }
            holeResponse.data?.data.let {
                if (it != null) {
                    insertCommentList(it)
                }
            }
        }
    }

    // 变更关注状态，重新获取这一条的记录并塞到数据库中
    suspend fun switchAttentionStatus(
        holeItemBean: HoleItemBean,
        switch: Int,
    ): HoleApiResponse<String?> {
        val holeResponse =
            launchRequest { holeApi.switchAttentionStatus(pid = holeItemBean.pid) }
        if (holeResponse.code == 0 || holeResponse.code == 20000) {
            // 表示操作成功，变更数据库表中数据
            Timber.e("switchAttentionStatus response %s", holeResponse.toString())
//                val currentHoleItem = getHoleItem(pid)
//                Todo：[为什么从数据库中取不出来数据？取出来结果导致无法继续下一步]
            Timber.e("attention modify before %s", holeItemBean.toString())
            holeItemBean.is_follow = switch
            if (switch == 0) {
                holeItemBean.likenum--
            } else {
                holeItemBean.likenum++
            }
            holeListDao.updateBean(holeItemBean)
//                currentHoleItem.map {
//                    if (it != null) {
//                        it.isAttention = switch
//                        holeListDao.updateBean(it)
//                    }
            Timber.e("attention modify after %s", holeItemBean.toString())
        }
        // Todo: 其实不需要return了
        return holeResponse
    }

    fun getTagItem(tid: Long) = tagDao.get(tid)
    fun getTagIdByTest(tagName: String) = tagDao.getTagIdByTest()
    fun getTagIdByName(tagName: String) = tagDao.getTagIdByName(tagName)
    fun getTagList(): Flow<List<TagBean>> = tagDao.getTagList()
    fun getTagNameList(): Flow<List<String>> = tagDao.getTagNameList()

    private suspend fun insertTagList(tagList: List<TagBean>) = tagDao.insertTagList(tagList)

    // 获取Tag列表，并插入到数据库中【这里不用更新】
    suspend fun getTagListFromNetToDatabase() {
        withContext(Dispatchers.IO) {
            val tagResponse = launchRequest { holeApi.getTagList() }
            tagResponse.data?.let { insertTagList(it) }
        }
    }


    // 清理该repository所有数据
    suspend fun clear() {
        holeListDao.clear()
        commentDao.clear()
        tagDao.clear()
    }


//    /**
//     * @param stateLiveData 带有请求状态的LiveData
//     */
//    suspend fun getHoleListFromNet(page: Int, stateLiveData: StateLiveData<List<HoleListItemBean>?>) {
//        executeResp(
//            { holeApi.getHoleList(page = page, token = "")}
//            , stateLiveData)
//    }

//    /**
//     * 返回的带结果的数据，系统Error使用Toast,接口Error使用catch处理
//     */
//    suspend fun getHoleListFromNetWithResult(page: Int): HoleApiResponse<List<HoleListItemBean>?> {
//        return holeApi.getHoleList(page = page, token = "")
//    }

    /**
     * 纯网络接口，不向数据库写数据
     * 评论成功返回数据
     */
    suspend fun sendReplyComment(pid: Long, comment: String): HoleApiResponse<EmptyBean?> {
        return launchRequest { holeApi.sendReplyComment(pid = pid, text = comment) }
    }

    /**
     * 举报，不向数据库写数据
     * 举报成功返回数据
     */
    suspend fun report(pid: Long, reason: String, token: String): HoleApiResponse<EmptyBean?> {
        return launchRequest { holeApi.report(pid = pid, reason = reason) }
    }

    suspend fun postHoleOnlyText(text: String, labelId: Long?): HoleApiResponse<EmptyBean?> {
        return launchRequest { holeApi.postHoleOnlyText(text = text, labelId = labelId) }
    }

    suspend fun postHoleWithImage(
        text: String,
        data: String,
        labelId: Long?
    ): HoleApiResponse<EmptyBean?> {
        return launchRequest {
            holeApi.postHoleWithImage(
                text = text,
                data = data,
                labelId = labelId
            )
        }
    }

    suspend fun search(
        keywords: String,
        page: Long,
        labelId: Long?
    ): HoleApiResponse<HoleListBody<HoleItemModel>?> {
//        var labelId = labelName?.let { name -> getTagIdByTest(name).tid }
        return launchRequest { holeApi.search(keywords = keywords, page = page, labelId = labelId) }
    }

    suspend fun searchPid(pid: String): HoleApiResponse<HoleListBody<HoleItemModel>?> {
        return launchRequest { holeApi.searchPid(pid = pid) }
    }

//    suspend fun login(account: String, password: String):HoleApiResponse<String?>{
//        return holeApi.login(uid = account, password = password)
//    }


    suspend fun loginSecure(account: String, passwordSecure: String): HoleApiResponse<UserInfo?> {
        return launchRequest { holeApi.loginSecure(uid = account, password = passwordSecure) }
    }

    suspend fun sendSMSValidCode(): HoleApiResponse<EmptyBean?> {
        return launchRequest { holeApi.sendSMSValidCode() }
    }

    suspend fun verifySMSValidCode(valid_code: String): HoleApiResponse<EmptyBean?> {
        return launchRequest { holeApi.verifySMSValidCode(valid_code = valid_code) }
    }

    suspend fun getRandomHoleManagementPractice(): HoleApiResponse<HoleManagementPracticeBean?> {
        return launchRequest { holeApi.getRandomHoleManagementPractice() }
    }

//    suspend fun getPictureData(pid: Long): HoleApiResponse<String?> {
//        return launchRequestPic { holeApi.getPictureFromPid(pid = pid) }
//    }

    suspend fun checkUpdate(): HoleApiResponse<UpdateInfo?> {
        return launchRequest { holeApi.checkUpdate() }
    }

}