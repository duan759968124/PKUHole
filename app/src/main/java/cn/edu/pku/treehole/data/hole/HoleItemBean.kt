package cn.edu.pku.treehole.data.hole

import androidx.room.*
import com.google.gson.annotations.SerializedName

/**
 *
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 *
 * 最大的实体，同时是数据库实体
 */
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity(
    tableName = "hole_list_table", )
//foreignKeys = [ForeignKey(
//        entity = TagBean::class,
//        childColumns = ["label"],
//        parentColumns = ["id"]
//    )], indices = [Index("pid")]
data class HoleItemBean(
    @field:SerializedName("pid")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pid")
    var pid: Long,

    @field:SerializedName("text")
    @ColumnInfo(name = "text")
    var text: String?,

    @field:SerializedName("type")
    @ColumnInfo(name = "type")
    var type: String,

//    Todo: 时间戳类型？？转化和格式
    @field:SerializedName("timestamp")
    @ColumnInfo(name = "timestamp")
    var timestamp: Long,

    @field:SerializedName("reply")
    @ColumnInfo(name = "reply")
    var reply: Int,

    @field:SerializedName("likenum")
    @ColumnInfo(name = "likenum")
    var likenum: Int,

    @field:SerializedName("extra")
    @ColumnInfo(name = "extra")
    var extra: Int?,

    @field:SerializedName("anonymous")
    @ColumnInfo(name = "anonymous")
    var anonymous: Int?,

    @field:SerializedName("is_top")
    @ColumnInfo(name = "is_top")
    var is_top: Int?,

    @field:SerializedName("is_follow")
    @ColumnInfo(name = "is_follow")
    var is_follow: Int?,  //本人是否关注 1 是 ，0 未关注

    @field:SerializedName("label")
    @ColumnInfo(name = "label")
    var label: Int?,


    @field:SerializedName("label_info")
////    @ColumnInfo(name = "label_info_id")
//    var label_info_id: Int?,
    @Embedded(prefix = "label_info_")
    var label_info: TagBean?,

    @field:SerializedName("tag")
    @ColumnInfo(name = "tag")
    var tag: String?,

    @ColumnInfo(name = "isHole")
    var isHole: Int?,

    @ColumnInfo(name = "isRead")
    var isRead: Int?,  // 0 or 1

){
//    @Ignore var tagInfo: TagBean? = null
}

