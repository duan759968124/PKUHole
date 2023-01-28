package cn.edu.pku.treehole.data.hole

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 *
 * @Time:           2022/01/01
 * @Desc:
 * @Version:        1.0
 */
@Dao
interface TagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tagBean: TagBean): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTagList(tagList: List<TagBean>): List<Long>

//    @Update
//    suspend fun updateList(commentList: List<HoleCommentItemBean>)

    @Query("Select * from tag_list_table where tid = :key")
    fun get(key: Long): Flow<TagBean?>

    @Query("Select * from tag_list_table ORDER BY tid ASC")
    fun getTagList(): Flow<List<TagBean>>

    @Query("Select tag_name from tag_list_table ORDER BY tid ASC")
    fun getTagNameList(): Flow<List<String>>

    @Query("Select * from tag_list_table where tid = 1 LIMIT 1")
    fun getTagIdByTest(): TagBean

    @Query("Select tid from tag_list_table where tag_name = :name")
    fun getTagIdByName(name: String): Flow<Long?>

    @Query("DELETE FROM tag_list_table")
    suspend fun clear()

}