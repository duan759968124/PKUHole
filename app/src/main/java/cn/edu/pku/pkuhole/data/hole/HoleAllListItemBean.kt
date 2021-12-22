package cn.edu.pku.pkuhole.data.hole

import androidx.room.ColumnInfo
import androidx.room.Entity

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
    @ColumnInfo(name = "hidden")
    var hidden: Int = 0,
    @ColumnInfo(name = "hot")
    var hot: Long = 0L
) : HoleItemBaseBean()