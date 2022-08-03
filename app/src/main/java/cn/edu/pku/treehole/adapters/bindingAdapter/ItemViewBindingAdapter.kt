package cn.edu.pku.treehole.adapters.bindingAdapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import cn.edu.pku.treehole.utilities.HOLE_HOST_ADDRESS
import cn.edu.pku.treehole.utilities.convertDurationToFormatted
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.io.File


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

@BindingAdapter(value = ["imageUrl", "imagePid"])
fun bindImageFromUrl(view: ImageView, imageUrl: String?, imagePid: Long?) {
    if (!imageUrl.isNullOrEmpty()) {
        val url = HOLE_HOST_ADDRESS + "api/pku_image/" + imagePid
        Glide.with(view.context)
            .load(url)
//            .placeholder(R.drawable.bg_drawer_header)
//            .error(R.drawable.ic_broken_image_24)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}


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