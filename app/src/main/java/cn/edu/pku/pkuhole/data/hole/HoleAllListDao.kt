package cn.edu.pku.pkuhole.data.hole

import androidx.lifecycle.LiveData
import androidx.room.*

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

    @Update
    suspend fun update(holeAllListItemBean: HoleAllListItemBean)

    @Query("Select * from hole_all_list_table where pid = :key")
    suspend fun get(key: Long): HoleAllListItemBean?

    @Query("Select * from hole_all_list_table ORDER BY pid DESC")
    fun getAllList(): LiveData<List<HoleAllListItemBean>>

    /**
     * Selects and returns the latest
     */
    @Query("SELECT * FROM hole_all_list_table ORDER BY pid DESC LIMIT 1")
    suspend fun getLatest(): HoleAllListItemBean?

    @Query("DELETE FROM hole_all_list_table")
    suspend fun clear()

    @Query("Select * from hole_all_list_table where pid = :key")
    fun getHoleDetailWithPid(key: Long): LiveData<HoleAllListItemBean>


}