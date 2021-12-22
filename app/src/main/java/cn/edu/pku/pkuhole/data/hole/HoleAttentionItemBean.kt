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
@Entity(tableName = "hole_attention_table")
data class HoleAttentionItemBean(
    @ColumnInfo(name = "attention_tag")
    var attentionTag: String? = ""
) : HoleItemBaseBean()