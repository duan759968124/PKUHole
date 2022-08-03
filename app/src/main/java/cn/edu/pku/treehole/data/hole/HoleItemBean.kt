package cn.edu.pku.treehole.data.hole

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 *
 * 最大的实体，同时是数据库实体
 */
@Entity(tableName = "hole_list_table")
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

    @field:SerializedName("url")
    @ColumnInfo(name = "url")
    var url: String?,

    @field:SerializedName("is_top")
    @ColumnInfo(name = "is_top")
    var is_top: Int?,

    @field:SerializedName("is_follow")
    @ColumnInfo(name = "is_follow")
    var is_follow: Int?,  //本人是否关注 1 是 ，0 未关注

//    @field:SerializedName("label_info")
////    @ColumnInfo(name = "label_info_id")
////    var label_info_id: Int?,
//    @Embedded(prefix = "label_info_")
//    var label_info: LabelInfoBean?,
    @field:SerializedName("label")
    @ColumnInfo(name = "label")
    var label: Int?,

    @field:SerializedName("tag")
    @ColumnInfo(name = "tag")
    var tag: String?,

    @ColumnInfo(name = "isHole")
    var isHole : Int?,
)


//fun List<HoleItemBean>.asDomainModel():List<HoleItemModel>{
//    return map{
//        HoleItemModel(
//            pid = it.pid,
//            text = it.text,
//            type = it.type,
//            timestamp = it.timestamp,
//            reply = it.reply,
//            likenum = it.likenum,
//            extra = it.extra,
//            url = it.url,
//            tag = it.tag,
//        )
//    }
//}
//
//fun HoleItemBean.asDomainModel():HoleItemModel{
//    return let{
//        HoleItemModel(
//            pid = it.pid,
//            text = it.text,
//            type = it.type,
//            timestamp = it.timestamp,
//            reply = it.reply,
//            likenum = it.likenum,
//            extra = it.extra,
//            url = it.url,
//            tag = it.tag,
//        )
//    }
//}

