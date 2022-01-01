package cn.edu.pku.pkuhole.data.hole

import androidx.lifecycle.Transformations
import cn.edu.pku.pkuhole.api.HoleApiResponse
import cn.edu.pku.pkuhole.api.HoleApiService
import cn.edu.pku.pkuhole.base.BaseRepository
import cn.edu.pku.pkuhole.base.network.StateLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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
    private val attentionDao: AttentionDao,
    private val holeApi : HoleApiService,
    ): BaseRepository() {

    fun getHoleItem(pid: Long) = holeListDao.get(pid)

    fun getAttentionItem(pid: Long) = attentionDao.get(pid)


    fun getHoleList() : Flow<List<HoleItemModel>> = holeListDao.getAllList().map { it.asDomainModel() }
    fun getAttentionList() : Flow<List<HoleItemModel>> = attentionDao.getAllList().map { it.asDomainModel() }

    suspend fun insertHoleItem(holeListItemBean: HoleListItemBean) = holeListDao.insert(holeListItemBean)

    suspend fun insertAttentionItem(attentionItemBean: AttentionItemBean) = attentionDao.insert(attentionItemBean)

    private suspend fun insertHoleList(holeListList: List<HoleListItemBean>) = holeListDao.insertAll(holeListList)

    private suspend fun insertAttentionList(attentionList: List<AttentionItemBean>) = attentionDao.insertAll(attentionList)

    suspend fun updateHoleItem(holeListItemBean: HoleListItemBean) = holeListDao.update(holeListItemBean)

    suspend fun updateAttentionItem(attentionItemBean: AttentionItemBean) = attentionDao.update(attentionItemBean)

    // Todo: 使用多个后端数据？比如清理就清理掉allList 和 attention的所有数据？包括HoleDetail
    suspend fun clearHoleList() = holeListDao.clear()

    suspend fun clearAttentionList() = attentionDao.clear()

    // Todo : 后续改为查找HoleDetail的表格
    fun getHoleDetailWithPid(pid : Long) = holeListDao.getHoleDetailWithPid(pid)

    // 记录获取第一页或者刷新时间，作为请求刷新的一个参数
    private var getFirstPageOrRefreshHoleListTimestamp : Long = 0L

    suspend fun getHoleListFromNetToDatabase(page: Int){
        withContext(Dispatchers.IO){
            val holeListResponse = holeApi.getHoleList(page = page)
            holeListResponse.data?.let { insertHoleList(it) }
            if(page == 1){
                holeListResponse.timestamp?.let { getFirstPageOrRefreshHoleListTimestamp = it }
            }

        }
    }

    suspend fun refreshHoleListFromNetToDatabase(){
        withContext(Dispatchers.IO){
            val refreshHoleListResponse = holeApi.refreshHoleList(timestamp = getFirstPageOrRefreshHoleListTimestamp)
            refreshHoleListResponse.data?.let { insertHoleList(it) }
            refreshHoleListResponse.timestamp?.let { getFirstPageOrRefreshHoleListTimestamp = it }

        }
    }

    // 获取attention列表  更换表格insert另一个表中
    suspend fun getAttentionListFromNetToDatabase(){
        withContext(Dispatchers.IO){
            val attentionListResponse = holeApi.getAttentionList()
            attentionListResponse.data?.let { insertAttentionList(it) }
        }
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