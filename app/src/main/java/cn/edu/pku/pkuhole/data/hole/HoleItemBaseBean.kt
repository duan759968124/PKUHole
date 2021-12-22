package cn.edu.pku.pkuhole.data.hole

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */
open class HoleItemBaseBean(
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
    var replyNum: Int = 0,
    @ColumnInfo(name = "extra")
    var extra: Int = 0,
    @ColumnInfo(name = "url")
    var url: String = "",
    @ColumnInfo(name = "likeNum")
    var likeNum: Int = 0,
    @ColumnInfo(name = "tag")
    var tag: String? = ""
)