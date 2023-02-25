package cn.edu.pku.treehole.adapters.bindingAdapter

import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import androidx.databinding.BindingAdapter
import cn.edu.pku.treehole.R
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.hole.CommentItemBean
import cn.edu.pku.treehole.data.hole.HoleItemBean
import cn.edu.pku.treehole.utilities.HOLE_HOST_ADDRESS
import cn.edu.pku.treehole.utilities.convertDurationToFormatted
import cn.edu.pku.treehole.utilities.golden_ratio_conjugate
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.color.MaterialColors
import timber.log.Timber
import java.io.File
import java.util.*


/**
 *
 * @Time:           2021/12/30
 * @Desc:
 * @Version:        1.0
 */
@BindingAdapter("postDurationFormatted")
fun TextView.setPostDurationFormatted(postTimestamp : Long){
    text = convertDurationToFormatted(postTimestamp, System.currentTimeMillis(), context.resources)
}

@BindingAdapter("hasImage")
fun bindHasImage(view: View, type: String?) {
    view.visibility = if (type != "image") {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("hasContentText")
fun bindHasContentText(view: View, text: String?) {
    view.visibility = if (text.isNullOrEmpty()) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("hasTag")
fun bindHasTag(view: View, labelId: Long?) {
    view.visibility = if (labelId == null || labelId == 0L) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("hasTagStr")
fun bindHasTagStr(view: View, tagStr: String?) {
    view.visibility = if (tagStr.isNullOrEmpty()) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("firstShow")
fun bindFirstShow(view: View, replyNum: Int) {
    view.visibility = if (replyNum > 0) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("secondShow")
fun bindSecondShow(view: View, replyNum: Int) {
    view.visibility = if (replyNum > 1) {
        View.VISIBLE
    } else {
        View.GONE
    }
}


@BindingAdapter("hasTopTag")
fun bindHasTopTag(view: View, holeItemBean: HoleItemBean?) {
    if (holeItemBean != null) {
        view.visibility = if (holeItemBean.is_top == 1 && holeItemBean.isHole == 1) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }else{
        view.visibility = View.GONE
    }
}

@BindingAdapter("showImage")
fun bindImageFromUrl(view: ImageView, holeItemBean: HoleItemBean?) {
    if (holeItemBean != null) {
        if (holeItemBean.type == "image") {
            val url = HOLE_HOST_ADDRESS + "api/pku_image/" + holeItemBean.pid
            val glideUrl = GlideUrl(
                url,
                LazyHeaders.Builder().addHeader("Authorization", "Bearer ${LocalRepository.getValidToken()}").build()
            )
//        val uri = imagePicData.split(',')[1]
//        val imageByteArray: ByteArray = Base64.decode(uri, Base64.DEFAULT)
//    val imageByteArray = imagePicData?.decodeBase64()
            Glide.with(view.context)
                .load(glideUrl)
//            .placeholder(R.drawable.bg_drawer_header)
//            .error(R.drawable.ic_broken_image_24)
//            .transition(DrawableTransitionOptions.withCrossFade())
                .into(view)
        }
    }

}

//val url:String="https://URL.com/" + data[position].image_path;
//
//val glideUrl = GlideUrl(
//    url,
//    LazyHeaders.Builder()
//        .addHeader("Authorization", "Bearer $token")
//        .build()
//)
//
//
//Glide.with(mContext)
//.load(glideUrl)
//.into(holder.binding.img);


@BindingAdapter("hasLocalImage")
fun bindHasLocalImage(view: View, file: File?) {
    if (file != null) {
        view.visibility = if (file.path.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}

@BindingAdapter("imageFromFile")
fun bindImageFromFile(view: ImageView, file: File?) {
    if (file != null) {
        if (file.path.isNotEmpty()) {
            Glide.with(view.context)
                .load(file)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view)
        }
    }
}

@BindingAdapter("showLzIcon")
fun showLzIcon(view: ImageView, isLz: Int?) {
    view.visibility = if (isLz == 1) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter(value = ["showDotIsHole", "showDotIsRead"])
fun showDot(view: ImageView, isHole: Int, isRead: Int) {
    view.visibility = if (isHole == 1 && isRead == 0) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("setBackgroundColor")
fun setBackgroundColor(view: ConstraintLayout, commentItemBean: CommentItemBean?) {
    var randomH: Double
    if (commentItemBean != null) {
        randomH = if(commentItemBean.randomH.isNaN()||commentItemBean.randomH.equals(0.0)){
            Math.random()
        }else{
            commentItemBean.randomH
        }
        when(commentItemBean.name.lowercase(Locale.getDefault())){
            "洞主" ->{
                if(LocalRepository.localUIDarkMode){
                    view.setBackgroundColor(arrayListOf(
                        ColorUtils.HSLToColor(floatArrayOf(0.0f, 0.0f, 0.97f)),
                        ColorUtils.HSLToColor(floatArrayOf(0.0f, 0.0f, 0.16f))
                    )[1])
                }else{
                    view.setBackgroundColor(arrayListOf(
                        ColorUtils.HSLToColor(floatArrayOf(0.0f, 0.0f, 0.97f)),
                        ColorUtils.HSLToColor(floatArrayOf(0.0f, 0.0f, 0.16f))
                    )[0])
                }

            }
            "alice" ->{
                randomH += golden_ratio_conjugate
                randomH %= 1
                if(LocalRepository.localUIDarkMode){
                    view.setBackgroundColor(arrayListOf(
                        ColorUtils.HSLToColor(floatArrayOf((randomH *360).toFloat(), 0.5f, 0.9f)),
                        ColorUtils.HSLToColor(floatArrayOf((randomH *360).toFloat(), 0.6f, 0.2f))
                    )[1])
                }else{
                    view.setBackgroundColor(arrayListOf(
                        ColorUtils.HSLToColor(floatArrayOf((randomH *360).toFloat(), 0.5f, 0.9f)),
                        ColorUtils.HSLToColor(floatArrayOf((randomH *360).toFloat(), 0.6f, 0.2f))
                    )[0])
                }

            }
            "bob" ->{
                randomH += golden_ratio_conjugate
                randomH %= 1
                randomH += golden_ratio_conjugate
                randomH %= 1
                if(LocalRepository.localUIDarkMode){
                    view.setBackgroundColor(arrayListOf(
                        ColorUtils.HSLToColor(floatArrayOf((randomH *360).toFloat(), 0.5f, 0.9f)),
                        ColorUtils.HSLToColor(floatArrayOf((randomH *360).toFloat(), 0.6f, 0.2f))
                    )[1])
                }else{
                    view.setBackgroundColor(arrayListOf(
                        ColorUtils.HSLToColor(floatArrayOf((randomH *360).toFloat(), 0.5f, 0.9f)),
                        ColorUtils.HSLToColor(floatArrayOf((randomH *360).toFloat(), 0.6f, 0.2f))
                    )[0])
                }
            }
            else ->{
                randomH = Math.random()
                if(LocalRepository.localUIDarkMode){
                    view.setBackgroundColor(arrayListOf(
                        ColorUtils.HSLToColor(floatArrayOf((randomH *360).toFloat(), 0.5f, 0.9f)),
                        ColorUtils.HSLToColor(floatArrayOf((randomH *360).toFloat(), 0.6f, 0.2f))
                    )[1])
                }else{
                    view.setBackgroundColor(arrayListOf(
                        ColorUtils.HSLToColor(floatArrayOf((randomH *360).toFloat(), 0.5f, 0.9f)),
                        ColorUtils.HSLToColor(floatArrayOf((randomH *360).toFloat(), 0.6f, 0.2f))
                    )[0])
                }
            }
        }
    }
}



//@BindingAdapter("fillNavHeader")
//fun fillNavHeader(view: NavigationView, userInfo: UserInfo?){
//    val binding = NavHeaderMainBinding.inflate(LayoutInflater.from(view.context))
//    if(userInfo != null){
//        binding.userInfo = userInfo
//    }else{
//        binding.userInfo = UserInfo(uid = "00", name = "unknown", department = "unknown", token = "", token_timestamp = 0L)
//    }
//    binding.executePendingBindings()
//    view.addHeaderView(binding.root)
//}

@BindingAdapter("backgroundAttr")
fun setBackgroundAttr(view: View , isSelected: Boolean) {
    val typedValueColor = TypedValue()
    view.context.theme.resolveAttribute(R.attr.colorPrimary, typedValueColor, true);
    if(isSelected){
        view.setBackgroundColor(typedValueColor.data)
    } else {
        view.setBackgroundColor(view.context.resources.getColor(R.color.gray_500));
    }
}

