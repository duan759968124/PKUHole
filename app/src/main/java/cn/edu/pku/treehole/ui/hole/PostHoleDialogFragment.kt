package cn.edu.pku.treehole.ui.hole

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.impl.utils.ContextUtil.getApplicationContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import cn.edu.pku.treehole.R
import cn.edu.pku.treehole.databinding.DialogHolePostBinding
import cn.edu.pku.treehole.utilities.GlideEngine
import cn.edu.pku.treehole.viewmodels.hole.HoleViewPagerViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.animators.AnimationType
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.style.PictureCropParameterStyle
import com.luck.picture.lib.style.PictureParameterStyle
import com.luck.picture.lib.style.PictureWindowAnimationStyle
import dagger.hilt.android.AndroidEntryPoint
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import timber.log.Timber


@AndroidEntryPoint
@RuntimePermissions
class PostHoleDialogFragment : DialogFragment() {
    private lateinit var binding: DialogHolePostBinding
    private val viewModel: HoleViewPagerViewModel by activityViewModels()

    private lateinit var mPictureParameterStyle: PictureParameterStyle
    private lateinit var mCropParameterStyle: PictureCropParameterStyle

    private lateinit var tagSheetDialog: MaterialDialog
    private var tagNameList = listOf<String>()
    private var searchTagName: String = ""

    //    override fun onCreateDialog(savedInstanceState: Bundle?) : Dialog{
//        return activity?.let {
//            val builder = AlertDialog.Builder(it)
//            val inflater = requireActivity().layoutInflater;
//            builder.setView(inflater.inflate(R.layout.hole_post_frag, null))
////            builder.setMessage("测试post hole msg")
////                .setPositiveButton("fire", DialogInterface.OnClickListener { dialog, i ->
////                    Timber.e("click dialog %d", i)
////                })
////                .setNegativeButton("cancle", DialogInterface.OnClickListener { dialog, i ->
////                    Timber.e("click cancel %d", i)
////                })
//            builder.create()
//        }?:throw IllegalStateException("activity cannot be null")

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        Timber.e("onCancel dialog")
        viewModel.clearContent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogHolePostBinding.inflate(inflater, container, false)
        context ?: return binding.root
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        getWeChatStyle()
//        binding.btnHolePostSend.setOnClickListener(DialogInterface.OnClickListener { dialog, id ->
//            Timber.e("click send btn %d", id)
//            dialog.dismiss()
//        })
//        binding.btnHolePostImage.setOnClickListener()
//        return inflater.inflate(R.layout.dialog_hole_post, container, false)
        viewModel.openPictureSelect.observe(viewLifecycleOwner) { openPicureGallery ->
            if (openPicureGallery) {
                openPicGalleryWithPermissionCheck()
            }
        }

        viewModel.tagNameList.observe(viewLifecycleOwner) {
            it?.let {
                tagNameList = it
            }
        }

        viewModel.showTagListDialog.observe(viewLifecycleOwner) {
            if(it){
                tagSheetDialog = context?.let { context ->
                    MaterialDialog(context)
                        .title(text = viewModel.tagTitle)
                        .listItems(items = tagNameList){ dialog, index, text ->
                            searchTagName = text as String
                            binding.tagListTv.text = searchTagName
                            viewModel.dismissTagListDialog(searchTagName)
                        }
                }!!
                tagSheetDialog.show()
            }
        }

        return binding.root
    }

    @SuppressLint("RestrictedApi")
    private fun getWeChatStyle() {
        // 相册主题
        mPictureParameterStyle = PictureParameterStyle()
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle.isChangeStatusBarFontColor = false
        // 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle.isOpenCompletedNumStyle = false
        // 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle.isOpenCheckNumStyle = true
        // 状态栏背景色
        mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#393a3e")
        // 相册列表标题栏背景色
        mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#393a3e")
        // 相册父容器背景色
        mPictureParameterStyle.pictureContainerBackgroundColor =
            ContextCompat.getColor(
                getApplicationContext(context!!),
                R.color.black
            )
        // 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle.pictureTitleUpResId = R.drawable.picture_icon_wechat_up
        // 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle.pictureTitleDownResId = R.drawable.picture_icon_wechat_down
        // 相册文件夹列表选中圆点
        mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval
        // 相册返回箭头
        mPictureParameterStyle.pictureLeftBackIcon = R.drawable.picture_icon_close
        // 标题栏字体颜色
        mPictureParameterStyle.pictureTitleTextColor =
            ContextCompat.getColor(getApplicationContext(context!!), R.color.picture_color_white)
        // 相册右侧按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        mPictureParameterStyle.pictureCancelTextColor =
            ContextCompat.getColor(getApplicationContext(context!!), R.color.picture_color_53575e)
        // 相册右侧按钮字体默认颜色
        mPictureParameterStyle.pictureRightDefaultTextColor =
            ContextCompat.getColor(getApplicationContext(context!!), R.color.picture_color_53575e)
        // 相册右侧按可点击字体颜色,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureRightSelectedTextColor =
            ContextCompat.getColor(getApplicationContext(context!!), R.color.picture_color_white)
        // 相册右侧按钮背景样式,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureUnCompleteBackgroundStyle =
            R.drawable.picture_send_button_default_bg
        // 相册右侧按钮可点击背景样式,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureCompleteBackgroundStyle = R.drawable.picture_send_button_bg
        // 选择相册目录背景样式
        mPictureParameterStyle.pictureAlbumStyle = R.drawable.picture_new_item_select_bg
        // 相册列表勾选图片样式
        mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_wechat_num_selector
        // 相册标题背景样式 ,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureWeChatTitleBackgroundStyle = R.drawable.picture_album_bg
        // 微信样式 预览右下角样式 ,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureWeChatChooseStyle = R.drawable.picture_wechat_select_cb
        // 相册返回箭头 ,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureWeChatLeftBackStyle = R.drawable.picture_icon_back
        // 相册列表底部背景色
        mPictureParameterStyle.pictureBottomBgColor =
            ContextCompat.getColor(getApplicationContext(context!!), R.color.picture_color_grey)
        // 已选数量圆点背景样式
        // 已选数量圆点背景样式
        mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        mPictureParameterStyle.picturePreviewTextColor =
            ContextCompat.getColor(getApplicationContext(context!!), R.color.picture_color_white)
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        mPictureParameterStyle.pictureUnPreviewTextColor =
            ContextCompat.getColor(getApplicationContext(context!!), R.color.picture_color_9b)
        // 相册列表已完成色值(已完成 可点击色值)
        mPictureParameterStyle.pictureCompleteTextColor =
            ContextCompat.getColor(getApplicationContext(context!!), R.color.picture_color_white)
        // 相册列表未完成色值(请选择 不可点击色值)
        mPictureParameterStyle.pictureUnCompleteTextColor =
            ContextCompat.getColor(getApplicationContext(context!!), R.color.picture_color_53575e)
        // 预览界面底部背景色
        mPictureParameterStyle.picturePreviewBottomBgColor =
            ContextCompat.getColor(getApplicationContext(context!!), R.color.picture_color_half_grey)
        // 外部预览界面删除按钮样式
        // 外部预览界面删除按钮样式
        mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalControlStyle =
            R.drawable.picture_original_wechat_checkbox
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalFontColor =
            ContextCompat.getColor(getApplicationContext(context!!), R.color.white)
        // 外部预览界面是否显示删除按钮
        mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true
        // 设置NavBar Color SDK Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP有效
        mPictureParameterStyle.pictureNavBarColor = Color.parseColor("#393a3e")

        // 完成文案是否采用(%1$d/%2$d)的字符串，只允许两个占位符哟
//        mPictureParameterStyle.isCompleteReplaceNum = true;
        // 自定义相册右侧文本内容设置
//        mPictureParameterStyle.pictureUnCompleteText = getString(R.string.app_wechat_send);
        //自定义相册右侧已选中时文案 支持占位符String 但只支持两个 必须isCompleteReplaceNum为true
//        mPictureParameterStyle.pictureCompleteText = getString(R.string.app_wechat_send_num);
//        // 自定义相册列表不可预览文字
//        mPictureParameterStyle.pictureUnPreviewText = "";
//        // 自定义相册列表预览文字
//        mPictureParameterStyle.picturePreviewText = "";
//        // 自定义预览页右下角选择文字文案
//        mPictureParameterStyle.pictureWeChatPreviewSelectedText = "";

//        // 自定义相册标题文字大小
//        mPictureParameterStyle.pictureTitleTextSize = 9;
//        // 自定义相册右侧文字大小
//        mPictureParameterStyle.pictureRightTextSize = 9;
//        // 自定义相册预览文字大小
//        mPictureParameterStyle.picturePreviewTextSize = 9;
//        // 自定义相册完成文字大小
//        mPictureParameterStyle.pictureCompleteTextSize = 9;
//        // 自定义原图文字大小
//        mPictureParameterStyle.pictureOriginalTextSize = 9;
//        // 自定义预览页右下角选择文字大小
//        mPictureParameterStyle.pictureWeChatPreviewSelectedTextSize = 9;

        // 裁剪主题

        // 完成文案是否采用(%1$d/%2$d)的字符串，只允许两个占位符哟
//        mPictureParameterStyle.isCompleteReplaceNum = true;
        // 自定义相册右侧文本内容设置
//        mPictureParameterStyle.pictureUnCompleteText = getString(R.string.app_wechat_send);
        //自定义相册右侧已选中时文案 支持占位符String 但只支持两个 必须isCompleteReplaceNum为true
//        mPictureParameterStyle.pictureCompleteText = getString(R.string.app_wechat_send_num);
//        // 自定义相册列表不可预览文字
//        mPictureParameterStyle.pictureUnPreviewText = "";
//        // 自定义相册列表预览文字
//        mPictureParameterStyle.picturePreviewText = "";
//        // 自定义预览页右下角选择文字文案
//        mPictureParameterStyle.pictureWeChatPreviewSelectedText = "";

//        // 自定义相册标题文字大小
//        mPictureParameterStyle.pictureTitleTextSize = 9;
//        // 自定义相册右侧文字大小
//        mPictureParameterStyle.pictureRightTextSize = 9;
//        // 自定义相册预览文字大小
//        mPictureParameterStyle.picturePreviewTextSize = 9;
//        // 自定义相册完成文字大小
//        mPictureParameterStyle.pictureCompleteTextSize = 9;
//        // 自定义原图文字大小
//        mPictureParameterStyle.pictureOriginalTextSize = 9;
//        // 自定义预览页右下角选择文字大小
//        mPictureParameterStyle.pictureWeChatPreviewSelectedTextSize = 9;

        // 裁剪主题
        mCropParameterStyle = PictureCropParameterStyle(
            ContextCompat.getColor(getApplicationContext(context!!), R.color.app_color_grey),
            ContextCompat.getColor(getApplicationContext(context!!), R.color.app_color_grey),
            Color.parseColor("#393a3e"),
            ContextCompat.getColor(getApplicationContext(context!!), R.color.white),
            mPictureParameterStyle.isChangeStatusBarFontColor
        )
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun openPicGallery() {
        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())
            .imageEngine(GlideEngine.createGlideEngine())
            .theme(R.style.picture_WeChat_style)
            .isWeChatStyle(true)// 是否开启微信图片选择风格
            .isUseCustomCamera(false)// 是否使用自定义相机
            .setLanguage(-1)// 设置语言，默认中文
            .isPageStrategy(true)// 是否开启分页策略 & 每页多少条；默认开启
            .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
            .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
            .setPictureWindowAnimationStyle(PictureWindowAnimationStyle())// 自定义相册启动退出动画
            .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)// 列表动画效果
            .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
            .maxSelectNum(1)// 最大图片选择数量
            .minSelectNum(0)// 最小选择数量
            .isCompress(true)
            .compressQuality(85)
            .minimumCompressSize(1024)
            .synOrAsy(false)//开启同步or异步压缩
            .imageSpanCount(4)// 每行显示个数
            .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
            .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
            .isOriginalImageControl(false)
            .isPreviewImage(true)// 是否可预览图片
            .selectionMode(PictureConfig.SINGLE)
            .isSingleDirectReturn(false)
            .isOpenClickSound(false)
            .isPreviewImage(true)
            .isCamera(false)
            .isZoomAnim(true)
            .forResult(MyResultCallback(viewModel))
    }


    /**
     * 返回结果回调
     */
    private class MyResultCallback(viewModel: HoleViewPagerViewModel) :
        OnResultCallbackListener<LocalMedia> {
       private var viewModelCallback = viewModel
        override fun onResult(result: List<LocalMedia>) {
            for (media in result) {
                Timber.e("是否压缩:" + media.isCompressed)
                Timber.e("压缩:" + media.compressPath)
                Timber.e("名称:" + media.fileName)
                Timber.e("原图:" + media.path)
                Timber.e("绝对路径:" + media.realPath)
                Timber.e("是否裁剪:" + media.isCut)
                Timber.e("裁剪:" + media.cutPath)
                Timber.e("是否开启原图:" + media.isOriginal)
                Timber.e("原图路径:" + media.originalPath)
                Timber.e("Android Q 特有Path:" + media.androidQToPath)
                Timber.e("宽高: " + media.width + "x" + media.height)
                Timber.e("Size: " + media.size/1024)
                Timber.e("mimeType" + media.mimeType)
            }
            if(result.isNotEmpty()){
                viewModelCallback.finishSelectPicture(result[0])
            }

        }
        override fun onCancel() {
            Timber.e("PictureSelector Cancel")
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}