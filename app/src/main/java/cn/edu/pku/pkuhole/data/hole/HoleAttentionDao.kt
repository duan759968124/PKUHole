package cn.edu.pku.pkuhole.data.hole

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */
@Dao
interface HoleAttentionDao {

    @Insert
    suspend fun insert(holeAttentionItemBean: HoleAttentionItemBean)

    @Update
    suspend fun update(holeAttentionItemBean: HoleAttentionItemBean)

    @Query("Select * from hole_attention_table where pid = :key")
    suspend fun get(key: Long): HoleAttentionItemBean

    @Query("Select * from hole_attention_table ORDER BY timestamp DESC")
    fun getAllList(): LiveData<List<HoleAttentionItemBean>>

    /**
     * Selects and returns the latest
     */
    @Query("SELECT * FROM hole_attention_table ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatest(): HoleAttentionItemBean?

    @Query("DELETE FROM hole_attention_table")
    suspend fun clear()


}