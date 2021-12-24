package cn.edu.pku.pkuhole.data.hole

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */
@Dao
interface HoleAllListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(holeAllListItemBean: HoleAllListItemBean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(holeItems: List<HoleAllListItemBean>)

    @Update
    suspend fun update(holeAllListItemBean: HoleAllListItemBean)

    @Query("Select * from hole_all_list_table where pid = :key")
    fun get(key: Long): Flow<HoleAllListItemBean?>

    @Query("Select * from hole_all_list_table ORDER BY pid DESC")
    fun getAllList(): Flow<List<HoleAllListItemBean>>

//    /**
//     * Selects and returns the latest
//     */
//    @Query("SELECT * FROM hole_all_list_table ORDER BY pid DESC LIMIT 1")
//    suspend fun getLatest(): Flow<HoleAllListItemBean?>

    // Todo : 后续改为查找HoleDetail的表格
    @Query("Select * from hole_all_list_table where pid = :key")
    fun getHoleDetailWithPid(key: Long): Flow<HoleAllListItemBean>

    @Query("DELETE FROM hole_all_list_table")
    suspend fun clear()

}