package cn.edu.pku.treehole.ui.hole

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import cn.edu.pku.treehole.R
import cn.edu.pku.treehole.databinding.DialogHolePostBinding
import cn.edu.pku.treehole.utilities.GlideEngine
import cn.edu.pku.treehole.viewmodels.hole.HoleViewPagerViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.luck.picture.lib.animators.AnimationType
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.language.LanguageConfig
import com.luck.picture.lib.style.BottomNavBarStyle
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.style.SelectMainStyle
import com.luck.picture.lib.style.TitleBarStyle
import com.luck.picture.lib.utils.DateUtils
import com.luck.picture.lib.utils.DensityUtil
import dagger.hilt.android.AndroidEntryPoint
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import timber.log.Timber
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File


@AndroidEntryPoint
@RuntimePermissions
class PostHoleDialogFragment : DialogFragment() {
    private lateinit var binding: DialogHolePostBinding
    private val viewModel: HoleViewPagerViewModel by activityViewModels()

    private lateinit var mPictureSelectorStyle: PictureSelectorStyle
//    private lateinit var mPictureParameterStyle: PictureParameterStyle
//    private lateinit var mCropParameterStyle: PictureCropParameterStyle

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
        viewModel.openPictureSelect.observe(viewLifecycleOwner) { openPictureGallery ->
            if (openPictureGallery) {
                openPicGalleryWithPermissionCheck()
            }
        }

        viewModel.tagNameList.observe(viewLifecycleOwner) {
            it?.let {
                tagNameList = it
                tagSheetDialog = context?.let { context ->
                    MaterialDialog(context)
                        .title(text = viewModel.tagTitle)
                        .listItems(items = tagNameList){ dialog, index, text ->
                            searchTagName = text as String
                            binding.tagListTv.text = searchTagName
                            viewModel.dismissTagListDialog(searchTagName)
                        }
                        .cancelable(false)
                        .cancelOnTouchOutside(false)
                        .negativeButton(R.string.cancelReply)
                }!!
            }
        }

        viewModel.showTagListDialog.observe(viewLifecycleOwner) {
            if(it){
                tagSheetDialog.show()
            }else{
                tagSheetDialog.dismiss()
            }
        }

        return binding.root
    }

    private fun getWeChatStyle() {
        mPictureSelectorStyle = PictureSelectorStyle()
        val numberSelectMainStyle = SelectMainStyle()
        numberSelectMainStyle.isSelectNumberStyle = true
        numberSelectMainStyle.isPreviewSelectNumberStyle = false
        numberSelectMainStyle.isPreviewDisplaySelectGallery = true
        numberSelectMainStyle.selectBackground = R.drawable.ps_default_num_selector
        numberSelectMainStyle.previewSelectBackground = R.drawable.ps_preview_checkbox_selector
        numberSelectMainStyle.selectNormalBackgroundResources =
            R.drawable.ps_select_complete_normal_bg
        numberSelectMainStyle.selectNormalTextColor =
            ContextCompat.getColor(context!!, R.color.ps_color_53575e)
        numberSelectMainStyle.selectNormalText = getString(R.string.ps_send)
        numberSelectMainStyle.adapterPreviewGalleryBackgroundResource =
            R.drawable.ps_preview_gallery_bg
        numberSelectMainStyle.adapterPreviewGalleryItemSize = DensityUtil.dip2px(context, 52F)
        numberSelectMainStyle.previewSelectText = getString(R.string.ps_select)
        numberSelectMainStyle.previewSelectTextSize = 14
        numberSelectMainStyle.previewSelectTextColor =
            ContextCompat.getColor(context!!, R.color.ps_color_white)
        numberSelectMainStyle.previewSelectMarginRight = DensityUtil.dip2px(context, 6F)
        numberSelectMainStyle.selectBackgroundResources = R.drawable.ps_select_complete_bg
        numberSelectMainStyle.selectText = getString(R.string.ps_send_num)
        numberSelectMainStyle.selectTextColor =
            ContextCompat.getColor(context!!, R.color.ps_color_white)
        numberSelectMainStyle.mainListBackgroundColor =
            ContextCompat.getColor(context!!, R.color.ps_color_black)
        numberSelectMainStyle.isCompleteSelectRelativeTop = true
        numberSelectMainStyle.isPreviewSelectRelativeBottom = true
        numberSelectMainStyle.isAdapterItemIncludeEdge = false

        // 头部TitleBar 风格
        val numberTitleBarStyle = TitleBarStyle()
        numberTitleBarStyle.isHideCancelButton = true
        numberTitleBarStyle.isAlbumTitleRelativeLeft = true
        numberTitleBarStyle.titleAlbumBackgroundResource = R.drawable.ps_album_bg
        numberTitleBarStyle.titleDrawableRightResource = R.drawable.ps_ic_grey_arrow
        numberTitleBarStyle.previewTitleLeftBackResource = R.drawable.ps_ic_normal_back

        // 底部NavBar 风格
        val numberBottomNavBarStyle = BottomNavBarStyle()
        numberBottomNavBarStyle.bottomPreviewNarBarBackgroundColor = ContextCompat.getColor(
            context!!, R.color.ps_color_half_grey)
        numberBottomNavBarStyle.bottomPreviewNormalText = getString(R.string.ps_preview)
        numberBottomNavBarStyle.bottomPreviewNormalTextColor =
            ContextCompat.getColor(context!!, R.color.ps_color_9b)
        numberBottomNavBarStyle.bottomPreviewNormalTextSize = 16
        numberBottomNavBarStyle.isCompleteCountTips = false
        numberBottomNavBarStyle.bottomPreviewSelectText = getString(R.string.ps_preview_num)
        numberBottomNavBarStyle.bottomPreviewSelectTextColor =
            ContextCompat.getColor(context!!, R.color.ps_color_white)

        mPictureSelectorStyle.titleBarStyle = numberTitleBarStyle
        mPictureSelectorStyle.bottomBarStyle = numberBottomNavBarStyle
        mPictureSelectorStyle.selectMainStyle = numberSelectMainStyle
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun openPicGallery() {
        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofImage())
            .setImageEngine(GlideEngine.createGlideEngine())
            .setCompressEngine(ImageFileCompressEngine())
            .setSelectorUIStyle(mPictureSelectorStyle)
            .setLanguage(LanguageConfig.SYSTEM_LANGUAGE)
            .isPageStrategy(true)// 是否开启分页策略 & 每页多少条；默认开启
            .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)// 列表动画效果
            .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
            .setMaxSelectNum(1)// 最大图片选择数量
            .setMinSelectNum(0)// 最小选择数量
//            .setCompressEngine(true)
//            .compressQuality(85)
//            .minimumCompressSize(1024)
//            .synOrAsy(false)//开启同步or异步压缩
            .setImageSpanCount(4)// 每行显示个数
//            .setIsReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
            .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
//            .isOriginalImageControl(false)
            .isPreviewImage(true)// 是否可预览图片
            .setSelectionMode(SelectModeConfig.SINGLE)
//            .isSingleDirectReturn(false)
            .isOpenClickSound(false)
            .isPreviewImage(true)
            .isDirectReturnSingle(false)
//            .isCamera(false)
//            .isZoomAnim(true)
            .forResult(MyResultCallback(viewModel))
    }


    /**
     * 返回结果回调
     */
    private class MyResultCallback(viewModel: HoleViewPagerViewModel) :
        OnResultCallbackListener<LocalMedia> {
       private var viewModelCallback = viewModel
        override fun onResult(result: ArrayList<LocalMedia>) {
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
                Timber.e("Android Q 特有Path:" + media.sandboxPath)
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
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    /**
     * 自定义压缩
     */
    private class ImageFileCompressEngine : CompressFileEngine {
        override fun onStartCompress(
            context: Context?,
            source: ArrayList<Uri?>?,
            call: OnKeyValueResultCallbackListener?,
        ) {
            Luban.with(context).load(source).ignoreBy(800)
                .setRenameListener { filePath ->
                    val indexOf = filePath.lastIndexOf(".")
                    val postfix = if (indexOf != -1) filePath.substring(indexOf) else ".jpg"
                    DateUtils.getCreateFileName("CMP_").toString() + postfix
                }.setCompressListener(object : OnNewCompressListener {
                    override fun onStart() {}
                    override fun onSuccess(source: String?, compressFile: File) {
                        call?.onCallback(source, compressFile.getAbsolutePath())
                    }

                    override fun onError(source: String?, e: Throwable?) {
                        call?.onCallback(source, null)
                    }
                }).launch()
        }
    }
}