package cn.edu.pku.pkuhole.data.hole

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */
@Entity(tableName = "hole_all_list_table")
data class HoleAllListItemBean(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pid")
    var pid: Long = 0L,
    @ColumnInfo(name = "text")
    var text: String? = "",
    @ColumnInfo(name = "type")
    var type: String = "",
    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0L,
    @ColumnInfo(name = "reply")
    var reply: Int = 0,
    @ColumnInfo(name = "extra")
    var extra: Int = 0,
    @ColumnInfo(name = "url")
    var url: String? = "",
    @ColumnInfo(name = "likeNum")
    var likenum: Int = 0,
    @ColumnInfo(name = "tag")
    var tag: String?,
    @ColumnInfo(name = "hidden")
    var hidden: Int = 0,
    @ColumnInfo(name = "hot")
    var hot: Long = 0L
)