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
interface HoleListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(holeListItemBean: HoleListItemBean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(holeListItems: List<HoleListItemBean>)

    @Update
    suspend fun update(holeListItemBean: HoleListItemBean)

    @Query("Select * from hole_list_table where pid = :key")
    fun get(key: Long): Flow<HoleListItemBean?>

    @Query("Select * from hole_list_table ORDER BY pid DESC")
    fun getAllList(): Flow<List<HoleListItemBean>>

//    /**
//     * Selects and returns the latest
//     */
//    @Query("SELECT * FROM hole_list_table ORDER BY pid DESC LIMIT 1")
//    suspend fun getLatest(): Flow<HoleAllListItemBean?>

    // Todo : 后续改为查找HoleDetail的表格
    @Query("Select * from hole_list_table where pid = :key")
    fun getHoleDetailWithPid(key: Long): Flow<HoleListItemBean>

    @Query("DELETE FROM hole_list_table")
    suspend fun clear()

}