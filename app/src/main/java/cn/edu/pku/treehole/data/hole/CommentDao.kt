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
interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(commentItemBean: CommentItemBean): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommentList(commentList: List<CommentItemBean>): List<Long>

//    @Update
//    suspend fun updateList(commentList: List<HoleCommentItemBean>)

//    @Query("Select * from comment_table where cid = :key")
//    fun get(key: Long): Flow<AttentionItemBean?>

    @Query("Select * from comment_table where pid = :key ORDER BY cid ASC")
    fun getCommentListByPid(key: Long): Flow<List<CommentItemBean>>

    @Query("Select * from comment_table where pid = :key ORDER BY cid DESC")
    fun getCommentListByPidDesc(key: Long): Flow<List<CommentItemBean>>

    @Query("DELETE FROM comment_table")
    suspend fun clear()

}