package cn.edu.pku.pkuhole.data.hole

import androidx.lifecycle.LiveData
import cn.edu.pku.pkuhole.api.HoleApiResponse
import cn.edu.pku.pkuhole.api.HoleApiService
import cn.edu.pku.pkuhole.base.BaseRepository
import cn.edu.pku.pkuhole.base.network.StateLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/24
 * @Desc:
 * @Version:        1.0
 */

@Singleton
class HoleRepository @Inject constructor(
    private val holeListDao: HoleListDao,
    private val commentDao: CommentDao,
    private val holeApi : HoleApiService,
    ): BaseRepository() {

    fun getHoleList() : Flow<List<HoleItemBean>> = holeListDao.getHoleList()
    fun getAttentionList() : Flow<List<HoleItemBean>> = holeListDao.getAttentionList()

    private suspend fun updateOrInsertHoleList(holeListList: List<HoleListItemBean>) = holeListDao.upsertHoleList(holeListList)

    private suspend fun updateOrInsertAttentionList(attentionList: List<AttentionItemBean>) = holeListDao.upsertAttentionList(attentionList)


    // 记录获取第一页或者刷新时间，作为请求刷新的一个参数
    private var getFirstPageOrRefreshHoleListTimestamp : Long = 0L

    suspend fun getHoleListFromNetToDatabase(page: Int){
        withContext(Dispatchers.IO){
            val holeListResponse = holeApi.getHoleList(page = page)
            holeListResponse.data?.map { it.isHole = 1 }
            holeListResponse.data?.let { updateOrInsertHoleList(it) }
            if(page == 1){
                holeListResponse.timestamp?.let { getFirstPageOrRefreshHoleListTimestamp = it }
            }

        }
    }

    suspend fun refreshHoleListFromNetToDatabase(){
        withContext(Dispatchers.IO){
            val refreshHoleListResponse = holeApi.refreshHoleList(timestamp = getFirstPageOrRefreshHoleListTimestamp)
            refreshHoleListResponse.data?.map { it.isHole = 1 }
            refreshHoleListResponse.data?.let { updateOrInsertHoleList(it) }
            refreshHoleListResponse.timestamp?.let { getFirstPageOrRefreshHoleListTimestamp = it }

        }
    }

    // 获取attention列表  更换表格insert另一个表中
    suspend fun getAttentionListFromNetToDatabase(){
        withContext(Dispatchers.IO){
            val attentionListResponse = holeApi.getAttentionList()
            attentionListResponse.data?.map { it.isAttention = 1 }
            attentionListResponse.data?.let { updateOrInsertAttentionList(it) }
        }
    }

    // 树洞详情相关函数
    // update holeItemBean函数
    fun getHoleItem(pid: Long) = holeListDao.get(pid)
//    fun getHoleItem(pid: Long) = holeListDao.get(pid).map { it?.asDomainModel() }

    private suspend fun updateHoleItemModel(holeItemModel: HoleItemModel) = holeListDao.updateModel(holeItemModel)

    fun getCommentList(pid: Long) = commentDao.getCommentListByPid(pid)
    private suspend fun insertCommentList(commentList: List<CommentItemBean>) = commentDao.insertCommentList(commentList)

    // 获取一条树洞详情,【这条数据一定存在，并且isHole或者isAttention一定存在，所以直接更新】并更新数据库
    suspend fun getOneHoleFromNetToDatabase(pid: Long){
        withContext(Dispatchers.IO){
            val holeResponse = holeApi.getOneHole(pid = pid)
//            holeResponse.data?.let { it.reply = 100 }
            holeResponse.data?.let { updateHoleItemModel(it) }
        }
    }

    // 获取评论列表，并插入到数据库中【这里不用更新】
    suspend fun getCommentListFromNetToDatabase(pid: Long){
        withContext(Dispatchers.IO){
            val holeResponse = holeApi.getCommentList(pid = pid)
            holeResponse.data?.let { insertCommentList(it) }
        }
    }

    // 变更关注状态，重新获取这一条的记录并塞到数据库中
    suspend fun switchAttentionStatus(
        holeItemBean: HoleItemBean,
        switch: Int
    ): HoleApiResponse<String?> {
        val holeResponse =
            holeApi.switchAttentionStatus(pid = holeItemBean.pid, switch = switch)
        if (holeResponse.code == 0) {
            // 表示操作成功，变更数据库表中数据
            Timber.e("switchAttentionStatus response %s", holeResponse.toString())
//                val currentHoleItem = getHoleItem(pid)
//                Todo：[为什么从数据库中取不出来数据？取出来结果导致无法继续下一步]
            Timber.e("attention modify before %s", holeItemBean.toString())
            holeItemBean.isAttention = switch
            if(switch == 0){
                holeItemBean.likenum --
            }else{
                holeItemBean.likenum ++
            }
            holeListDao.updateBean(holeItemBean)
//                currentHoleItem.map {
//                    if (it != null) {
//                        it.isAttention = switch
//                        holeListDao.updateBean(it)
//                    }
            Timber.e("attention modify after %s", holeItemBean.toString())
        }
        return holeResponse
    }


    // 清理该repository所有数据
    suspend fun clear() {
        holeListDao.clear()
        commentDao.clear()
    }


    /**
     * @param stateLiveData 带有请求状态的LiveData
     */
    suspend fun getHoleListFromNet(page: Int, stateLiveData: StateLiveData<List<HoleListItemBean>>) {
        executeResp(
            { holeApi.getHoleList(page = page)}
            , stateLiveData)
    }

    /**
     * 返回的带结果的数据，系统Error使用Toast,接口Error使用catch处理
     */
    suspend fun getHoleListFromNetWithResult(page: Int): HoleApiResponse<List<HoleListItemBean>>? {
        return holeApi.getHoleList(page = page)
    }

    /**
     * 纯网络接口，不向数据库写数据
     * 评论成功返回数据
     */
    suspend fun sendReplyComment(pid: Long, comment: String): HoleApiResponse<Long> {
        return holeApi.sendReplyComment(pid = pid, text = comment)
    }

    /**
     * 举报，不向数据库写数据
     * 举报成功返回数据
     */
    suspend fun report(pid: Long, reason: String):HoleApiResponse<String?>{
        return holeApi.report(pid = pid, reason = reason)
    }

//    suspend fun login(account: String, password: String):HoleApiResponse<String?>{
//        return holeApi.login(uid = account, password = password)
//    }

    suspend fun login(account: String, password: String):HoleApiResponse<String?>{
        return launchRequest {holeApi.login(uid = account, password = password)}
    }

}