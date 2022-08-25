package cn.edu.pku.treehole.base

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.hole.HoleItemBean
import cn.edu.pku.treehole.utilities.GlideEngineNet
import cn.edu.pku.treehole.utilities.HOLE_HOST_ADDRESS
import cn.edu.pku.treehole.utilities.LoadingDialog
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.dialog.PictureCommonDialog
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnExternalPreviewEventListener
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.utils.DownloadFileUtils
import com.luck.picture.lib.utils.ToastUtils
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import timber.log.Timber


/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/29
 * @Desc:
 * @Version:        1.0
 */
//@AndroidEntryPoint
@RuntimePermissions
abstract class BaseFragment : Fragment() {

    private lateinit var mLoadingDialog: LoadingDialog
    private lateinit var mContext: Context
//    private val baseViewModel: BaseViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLoadingDialog = LoadingDialog(view.context, false)
        initObserve()
        initData()
    }

    open fun initObserve() {
//        baseViewModel.loginStatus.observe(viewLifecycleOwner, Observer { isLogin ->
//            if(!isLogin){
//                findNavController().navigate(R.id.action_global_nav_login)
//                baseViewModel.onNavigateToLoginFinish()
//            }
//        })
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    open fun previewPicture(holeItem: HoleItemBean) {
        if (holeItem.type == "image") {
            val localMedia = LocalMedia()
            val mPictureSelectorStyle = PictureSelectorStyle()
            localMedia.path = HOLE_HOST_ADDRESS + "api/pku_image/" + holeItem.pid
            localMedia.fileName = LocalRepository.getValidToken()
            localMedia.setPosition(0)
            val selectList: ArrayList<LocalMedia> = ArrayList(1)
            selectList.add(localMedia)
            PictureSelector.create(this)
                .openPreview()
                .setImageEngine(GlideEngineNet.createGlideEngine())
                .setSelectorUIStyle(mPictureSelectorStyle)
                .isHidePreviewDownload(false)
                .setExternalPreviewEventListener(object : OnExternalPreviewEventListener {
                    override fun onPreviewDelete(position: Int) {}
                    override fun onLongPressDownload(media: LocalMedia): Boolean {
                        return false
//                        // download picture dialog
//                        Timber.e("download picture!  out")
//                        val path = media.availablePath
//                        val dialog = PictureCommonDialog.showDialog(context,
//                            getString(R.string.ps_prompt),
//                            "保存图片到手机？")
//                        dialog.setOnDialogEventListener {
//                            if (PictureMimeType.isHasHttp(path)) {
//                                showLoading()
//                            }
//                            DownloadFileUtils.saveLocalFile(context, path, media.mimeType
//                            ) { realPath ->
//                                dismissLoading()
//                                if (TextUtils.isEmpty(realPath)) {
//                                    val errorMsg: String =
//                                        if (PictureMimeType.isHasAudio(media.mimeType)) {
//                                            getString(R.string.ps_save_audio_error)
//                                        } else if (PictureMimeType.isHasVideo(media.mimeType)) {
//                                            getString(R.string.ps_save_video_error)
//                                        } else {
//                                            getString(R.string.ps_save_image_error)
//                                        }
//                                    ToastUtils.showToast(context, errorMsg)
//                                } else {
//                                    PictureMediaScannerConnection(activity, realPath)
//                                    ToastUtils.showToast(context,
//                                        """
//                                                       ${getString(R.string.ps_save_success)}
//                                                       $realPath
//                                                       """.trimIndent())
//                                }
//                            }
//                        }
////                        activity?.let {
////                            MaterialDialog(it).show {
////                                title(text = "提醒")
////                                message(text = "保存图片到手机？")
////                                positiveButton(text = "确定") {
////                                    downloadPicture(media.fileName)
////                                }
////                                negativeButton(text = "取消")
////                            }
////                        }
//                        return true
                    }
                }).startActivityPreview(0, false, selectList)
//            PictureSelector.create(this)
//                .themeStyle(R.style.picture_WeChat_style)
//                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
////                todo:需要升级了，自己写下载图片的请求
//                .isNotPreviewDownload(true)
//                .imageEngine(GlideEngine.createGlideEngine())
//                .openExternalPreview(0, selectList)
//            findNavController().navigate(NavigationDirections.actionGlobalNavPreviewPicture(holeItem.pid))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    abstract fun initData()

    /**
     * show 加载中
     */
    fun showLoading() {
        mLoadingDialog.showDialog(mContext, false)
    }

    /**
     * dismiss loading dialog
     */
    fun dismissLoading() {
        mLoadingDialog.dismissDialog()
    }

    private var time: Long = 0
    private var oldMsg: String? = null

    /**
     * 相同msg 只显示一个。
     */
    fun showToast(msg: String) {
        if (msg != oldMsg) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
            time = System.currentTimeMillis()
        } else {
            if (System.currentTimeMillis() - time > 2000) {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
                time = System.currentTimeMillis()
            }
        }
        oldMsg = msg
    }
}