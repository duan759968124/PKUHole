package cn.edu.pku.pkuhole.data.hole

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

    fun getHoleList() : Flow<List<HoleItemModel>> = holeListDao.getHoleList().map { it.asDomainModel() }
    fun getAttentionList() : Flow<List<HoleItemModel>> = holeListDao.getAttentionList().map { it.asDomainModel() }
//    fun getAttentionList() : Flow<List<HoleItemModel>> = attentionDao.getAllList().map { it.asDomainModel() }

//    suspend fun insertHoleItem(holeListItemBean: HoleListItemBean) = holeListDao.insert(holeListItemBean)
//
//    suspend fun insertAttentionItem(attentionItemBean: AttentionItemBean) = attentionDao.insert(attentionItemBean)
//
//    private suspend fun insertHoleList(holeListList: List<HoleListItemBean>) = holeListDao.insertAll(holeListList)
//
//    private suspend fun insertAttentionList(attentionList: List<AttentionItemBean>) = attentionDao.insertAll(attentionList)
//
//    suspend fun updateHoleItem(holeListItemBean: HoleListItemBean) = holeListDao.update(holeListItemBean)
//
//    suspend fun updateAttentionItem(attentionItemBean: AttentionItemBean) = attentionDao.update(attentionItemBean)

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
    // update holeItemModel函数
    fun getHoleItem(pid: Long) = holeListDao.get(pid).map { it?.asDomainModel() }
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

}