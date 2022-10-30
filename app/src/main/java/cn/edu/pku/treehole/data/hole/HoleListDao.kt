package cn.edu.pku.treehole.data.hole

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

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(holeItemBean: HoleItemBean): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(holeItemBeans: List<HoleItemBean>): List<Long>

//    @Update(entity=HoleItemBean::class)
//    suspend fun <T> update(item: T)

    @Update(entity=HoleItemBean::class)
    suspend fun updateHole(item: HoleListItemBean)

    @Update(entity=HoleItemBean::class)
    suspend fun updateAttention(item: AttentionItemBean)

    @Update(entity=HoleItemBean::class)
    suspend fun updateModel(item: HoleItemModel)

    @Update(entity=HoleItemBean::class)
    suspend fun updateBean(item: HoleItemBean)

//    @Update(entity=HoleItemBean::class)
//    suspend fun <T> updateList(listItems: List<T>)

    @Update(entity=HoleItemBean::class)
    suspend fun updateHoleList(listItems: List<HoleListItemBean>)

    @Update(entity=HoleItemBean::class)
    suspend fun updateAttentionList(ListItems: List<AttentionItemBean>)

    @Update(entity=HoleItemBean::class)
    suspend fun updateModelList(ListItems: List<AttentionItemBean>)

    @Transaction
    suspend fun upsertHole(holeListItemBean: HoleListItemBean){
        val pid = insert(holeListItemBean.asDatabaseBean())
        if (pid == -1L){
            updateHole(holeListItemBean)
        }
    }

    @Transaction
    suspend fun upsertAttention(attentionItemBean: AttentionItemBean){
        val pid = insert(attentionItemBean.asDatabaseBean())
        if (pid == -1L){
            updateAttention(attentionItemBean)
        }
    }

    @Transaction
    suspend fun upsertModel(itemModel: HoleItemModel){
        val pid = insert(itemModel.asDatabaseBean())
        if (pid == -1L){
            updateModel(itemModel)
        }
    }

    @Transaction
    suspend fun upsertHoleList(holeListItems: List<HoleListItemBean>){
        val insertResult = insertAll(holeListItems.asDatabaseBean())
        val existList = mutableListOf<HoleListItemBean>()
        insertResult.mapIndexed { index, pid ->
            if(pid == -1L){
                existList.add(holeListItems[index])
            }
        }
        if(!existList.isNullOrEmpty()){
            updateHoleList(existList)
        }
    }

    @Transaction
    suspend fun upsertAttentionList(attentionItems: List<AttentionItemBean>){
        val insertResult = insertAll(attentionItems.asDatabaseBean())
        val existList = mutableListOf<AttentionItemBean>()
        insertResult.mapIndexed { index, pid ->
            if(pid == -1L){
                existList.add(attentionItems[index])
            }
        }
        if(!existList.isNullOrEmpty()){
            updateAttentionList(existList)
//            existList.map { updateAttention(it) }
        }
    }

    @Query("Select * from hole_list_table where pid = :key")
    fun get(key: Long): Flow<HoleItemBean?>

    @Query("Select * from hole_list_table where isHole = 1 ORDER BY is_top DESC, pid DESC")
    fun getHoleList(): Flow<List<HoleItemBean>>

    @Query("Select * from hole_list_table where is_follow = 1 ORDER BY pid DESC")
    fun getAttentionList(): Flow<List<HoleItemBean>>



    @Query("DELETE FROM hole_list_table")
    suspend fun clear()

}