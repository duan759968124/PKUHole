package cn.edu.pku.treehole.data.hole

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "comment_table")
data class CommentItemBean (
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

    @field:SerializedName("anonymous")
    @ColumnInfo(name = "anonymous")
    var anonymous: Int?,

    @field:SerializedName("tag")
    @ColumnInfo(name = "tag")
    var tag: String?,

    @field:SerializedName("islz")
    @ColumnInfo(name = "islz")
    var islz: Int,

    @field:SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String,

)