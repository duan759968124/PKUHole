package cn.edu.pku.pkuhole.data.hole

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */
@Dao
interface HoleListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(holeListItemBean: HoleListItemBean): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(holeListItems: List<HoleListItemBean>): List<Long>

    @Update
    suspend fun update(holeListItemBean: HoleListItemBean)


    @Update
    suspend fun updateList(holeListItems: List<HoleListItemBean>)

    @Transaction
    suspend fun upsert(holeListItemBean: HoleListItemBean){
        val pid = insert(holeListItemBean)
        if (pid == -1L){
            update(holeListItemBean)
        }
    }

    @Transaction
    suspend fun upsertList(holeListItems: List<HoleListItemBean>){
        val insertResult = insertAll(holeListItems)
        val existList = mutableListOf<HoleListItemBean>()
        Timber.e("insert result list %s", insertResult.toString())
        insertResult.mapIndexed { index, pid ->
            if(pid == -1L){
                existList.add(holeListItems[index])
            }
        }
        if(!existList.isNullOrEmpty()){
            val pid = existList[0].pid
            Timber.e("insert error need update net %s", existList[0].toString())
            updateList(existList)
        }
    }

    @Query("Select * from hole_list_table where pid = :key")
    fun get(key: Long): Flow<HoleListItemBean?>

    @Query("Select * from hole_list_table ORDER BY pid DESC")
    fun getAllList(): Flow<List<HoleListItemBean>>


    // Todo : 后续改为查找HoleDetail的表格
    @Query("Select * from hole_list_table where pid = :key")
    fun getHoleDetailWithPid(key: Long): Flow<HoleListItemBean>

    @Query("DELETE FROM hole_list_table")
    suspend fun clear()

}