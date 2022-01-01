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
interface AttentionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attentionItemBean: AttentionItemBean): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(attentionList: List<AttentionItemBean>): List<Long>

    @Update
    suspend fun update(attentionItemBean: AttentionItemBean)

    @Query("Select * from attention_table where pid = :key")
    fun get(key: Long): Flow<AttentionItemBean?>

    @Query("Select * from attention_table ORDER BY pid DESC")
    fun getAllList(): Flow<List<AttentionItemBean>>


    @Query("DELETE FROM attention_table")
    suspend fun clear()

}