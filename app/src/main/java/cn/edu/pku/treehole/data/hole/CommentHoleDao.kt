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
interface CommentHoleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommentHole(commentItemBeanHole: CommentItemBeanHole): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommentHoleList(commentHoleList: List<CommentItemBeanHole>): List<Long>

    @Query("Select * from comment_table_hole  where pid = :key ORDER BY cid ASC limit 1")
    fun getFirstCommentByPid(key: Long): Flow<CommentItemBeanHole?>

    @Query("Select * from comment_table_hole  where pid = :key ORDER BY cid ASC limit 1,1")
    fun getSecondCommentByPid(key: Long): Flow<CommentItemBeanHole?>

    @Query("DELETE FROM comment_table_hole")
    suspend fun clearCommentTableHole()

}