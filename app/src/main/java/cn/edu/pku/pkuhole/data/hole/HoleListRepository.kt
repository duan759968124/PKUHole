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

    suspend fun getHoleListFromNet(page: Int){
        withContext(Dispatchers.IO){
            Timber.e("add Hole List is called")
            val holeListPage = holeApi.getHoleList(page = page)
            holeListPage.data?.let { insertAll(it) }
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