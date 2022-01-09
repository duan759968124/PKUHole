package cn.edu.pku.pkuhole.adapters.bindingAdapter

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import cn.edu.pku.pkuhole.R
import cn.edu.pku.pkuhole.data.UserInfo
import cn.edu.pku.pkuhole.data.hole.HoleListItemBean
import cn.edu.pku.pkuhole.databinding.NavHeaderMainBinding
import cn.edu.pku.pkuhole.utilities.HOLE_HOST_ADDRESS
import cn.edu.pku.pkuhole.utilities.convertDurationToFormatted
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.navigation.NavigationView
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

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        val url = HOLE_HOST_ADDRESS + "services/pkuhole/images/" + imageUrl
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