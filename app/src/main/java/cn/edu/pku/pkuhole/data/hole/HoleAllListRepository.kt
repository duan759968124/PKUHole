package cn.edu.pku.pkuhole.data.hole

import cn.edu.pku.pkuhole.api.HoleApiService
import kotlinx.coroutines.flow.Flow
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
class HoleAllListRepository @Inject constructor(
    private val holeAllListDao: HoleAllListDao,
    private val holeApi : HoleApiService
    ) {
    fun getHoleItem(pid: Long) = holeAllListDao.get(pid)

    fun getAllList() = holeAllListDao.getAllList()

    suspend fun insert(holeAllListItemBean: HoleAllListItemBean) =
        holeAllListDao.insert(holeAllListItemBean)

    suspend fun insertAll(holeList: List<HoleAllListItemBean>) = holeAllListDao.insertAll(holeList)

    suspend fun update(holeAllListItemBean: HoleAllListItemBean) =
        holeAllListDao.update(holeAllListItemBean)

    // Todo: 使用多个后端数据？比如清理就清理掉allList 和 attention的所有数据？包括HoleDetail
    suspend fun clear() = holeAllListDao.clear()

    // Todo : 后续改为查找HoleDetail的表格
    fun getHoleDetailWithPid(pid : Long) = holeAllListDao.getHoleDetailWithPid(pid)

    // network fetch data
    suspend fun getHoleAllListFromNet(p: Int) : HoleApiResponse<List<HoleAllListItemBean>>{
        return holeApi.getHoleAllList(page = p)
    }
}