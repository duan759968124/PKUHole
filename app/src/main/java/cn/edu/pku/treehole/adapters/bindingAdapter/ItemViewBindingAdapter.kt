package cn.edu.pku.treehole.adapters.bindingAdapter

import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import cn.edu.pku.treehole.R
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.utilities.HOLE_HOST_ADDRESS
import cn.edu.pku.treehole.utilities.convertDurationToFormatted
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.io.File
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import okio.ByteString.Companion.decodeBase64


/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/30
 * @Desc:
 * @Version:        1.0
 */
@BindingAdapter("postDurationFormatted")
fun TextView.setPostDurationFormatted(postTimestamp : Long){
    text = convertDurationToFormatted(postTimestamp, System.currentTimeMillis(), context.resources)
}

@BindingAdapter("hasImage")
fun bindHasImage(view: View, url: String?) {
    view.visibility = if (url.isNullOrEmpty()) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("imagePicData")
fun bindImageFromUrl(view: ImageView, imagePicData: String?) {
    if (!imagePicData.isNullOrEmpty()) {
//        val url = HOLE_HOST_ADDRESS + "api/pku_image/" + imagePid
//        val glideUrl = GlideUrl(
//            url,
//    LazyHeaders.Builder().addHeader("Authorization", "Bearer ${LocalRepository.getValidToken()}").build()
//        )
        val uri = imagePicData.split(',')[1]
        val imageByteArray: ByteArray = Base64.decode(uri, Base64.DEFAULT)
//    val imageByteArray = imagePicData?.decodeBase64()
        Glide.with(view.context)
            .asBitmap()
            .load(imageByteArray)
            .placeholder(R.drawable.bg_drawer_header)
//            .error(R.drawable.ic_broken_image_24)
//            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
//        val decodedString: ByteArray = Base64.decode(imagePicData, Base64.DEFAULT)
//        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//        view.setImageBitmap(decodedByte)
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

//@BindingAdapter("handleHoleText")
//fun bindHandleHoleText(view: TextView, holeText: String?){
//    if (holeText != null) {
//        if(holeText.isNotEmpty()){
//            val spannableString = SpannableString(holeText)
//            //正则查找所有的树洞号，并标记下来，设置为可点击
//            val holeNumMap = regexHoleText(holeText)
//            holeNumMap.forEach { (value, indexRange) ->
//                val pid = value.substring(1)
//                Timber.e("$pid  $indexRange")
//                spannableString.setSpan(HoleTextClickSpan(pid), indexRange.first, indexRange.last + 1,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//            }
//            view.text = spannableString
//            view.movementMethod = LinkMovementMethod()
//
//        }
//    }
//}


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