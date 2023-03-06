package cn.edu.pku.treehole.data.hole

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "comment_table_hole")
data class CommentItemBeanHole (
    @field:SerializedName("cid")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "cid")
    var cid: Long,

    @field:SerializedName("pid")
    @ColumnInfo(name = "pid")
    var pid: Long,

    @field:SerializedName("text")
    @ColumnInfo(name = "text")
    var text: String?,

    @field:SerializedName("timestamp")
    @ColumnInfo(name = "timestamp")
    var timestamp: Long,

    @field:SerializedName("tag")
    @ColumnInfo(name = "tag")
    var tag: String?,

    @field:SerializedName("islz")
    @ColumnInfo(name = "islz")
    var islz: Int,

    @field:SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String?,

    @field:SerializedName("comment_id")
    @ColumnInfo(name = "comment_id")
    var commentId: Long?,

    @field:SerializedName("quote")
    @Embedded(prefix = "quote_")
    var quote: QuoteBean?,

//    "quote": {
//    "pid": 4645840,
//    "text": "fasdffafasdfsafd",
//    "name_tag": "Bob",
//    "name": null
//}

    @ColumnInfo(name = "random_h", defaultValue = "0.0")
    var randomH: Double,

)