package cn.edu.pku.pkuhole.data.hole

import android.widget.Toast
import cn.edu.pku.pkuhole.api.HoleApiResponse
import cn.edu.pku.pkuhole.api.HoleApiService
import cn.edu.pku.pkuhole.base.BaseRepository
import cn.edu.pku.pkuhole.base.network.StateLiveData
import cn.edu.pku.pkuhole.utilities.ToastUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.sql.Timestamp
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
class HoleListRepository @Inject constructor(
    private val holeListDao: HoleListDao,
    private val holeApi : HoleApiService
    ): BaseRepository() {
    fun getHoleItem(pid: Long) = holeListDao.get(pid)

    fun getHoleList() = holeListDao.getAllList()

    suspend fun insert(holeListItemBean: HoleListItemBean) =
        holeListDao.insert(holeListItemBean)

    suspend fun insertAll(holeListList: List<HoleListItemBean>) = holeListDao.insertAll(holeListList)

    suspend fun update(holeListItemBean: HoleListItemBean) =
        holeListDao.update(holeListItemBean)

    // Todo: 使用多个后端数据？比如清理就清理掉allList 和 attention的所有数据？包括HoleDetail
    suspend fun clear() = holeListDao.clear()

    // Todo : 后续改为查找HoleDetail的表格
    fun getHoleDetailWithPid(pid : Long) = holeListDao.getHoleDetailWithPid(pid)

    // network fetch data
//    suspend fun getHoleAllListFromNet(p: Int) : HoleApiResponse<List<HoleListItemBean>> {
//        return holeApi.getHoleList(page = p)
//    }
    // 记录获取第一页或者刷新时间，作为请求刷新的一个参数
    var getFirstPageOrRefreshHoleListTimestamp : Long = 0L

    suspend fun getHoleListFromNetToDatabase(page: Int){
        withContext(Dispatchers.IO){
            val holeListResponse = holeApi.getHoleList(page = page)
            holeListResponse.data?.map{  it.isHole = true }
            holeListResponse.data?.let { insertAll(it) }
            if(page == 1){
                holeListResponse.timestamp?.let { getFirstPageOrRefreshHoleListTimestamp = it }
            }

        }
    }

    suspend fun refreshHoleListFromNetToDatabase(){
        withContext(Dispatchers.IO){
            val refreshHoleListResponse = holeApi.refreshHoleList(timestamp = getFirstPageOrRefreshHoleListTimestamp)
            refreshHoleListResponse.data?.map { it.isHole = true }
            refreshHoleListResponse.data?.let { insertAll(it) }
            refreshHoleListResponse.timestamp?.let { getFirstPageOrRefreshHoleListTimestamp = it }

        }
    }



    /**
     * 请求项目分类。
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