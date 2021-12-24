package cn.edu.pku.pkuhole.data.hole

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
class HoleAllListRepository @Inject constructor(private val holeAllListDao: HoleAllListDao) {
    fun getHoleItem(pid: Long) = holeAllListDao.get(pid)

    fun getAllList() = holeAllListDao.getAllList()

    suspend fun insert(holeAllListItemBean: HoleAllListItemBean) =
        holeAllListDao.insert(holeAllListItemBean)

    suspend fun insertAll(holeList: List<HoleAllListItemBean>) = holeAllListDao.insertAll(holeList)

    suspend fun update(holeAllListItemBean: HoleAllListItemBean) =
        holeAllListDao.update(holeAllListItemBean)

    // Todo: 使用多个后端数据？比如清理就清理掉allList 和 attention的所有数据？包括HoleDetail
    suspend fun clear() = holeAllListDao.clear()
}