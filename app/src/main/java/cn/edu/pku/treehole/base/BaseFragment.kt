package cn.edu.pku.treehole.base

import android.Manifest
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cn.edu.pku.treehole.NavigationDirections
import cn.edu.pku.treehole.R
import cn.edu.pku.treehole.data.hole.HoleItemBean
import cn.edu.pku.treehole.utilities.GlideEngine
import cn.edu.pku.treehole.utilities.HOLE_HOST_ADDRESS
import cn.edu.pku.treehole.utilities.LoadingDialog
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions

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
abstract class BaseFragment: Fragment() {

    private lateinit var mLoadingDialog: LoadingDialog
    private lateinit var mContext: Context
//    private val baseViewModel: BaseViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLoadingDialog = LoadingDialog(view.context, false)
        initObserve()
        initData()
    }

    open fun initObserve(){
//        baseViewModel.loginStatus.observe(viewLifecycleOwner, Observer { isLogin ->
//            if(!isLogin){
//                findNavController().navigate(R.id.action_global_nav_login)
//                baseViewModel.onNavigateToLoginFinish()
//            }
//        })
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    open fun previewPicture(holeItem: HoleItemBean) {
        if(!holeItem.pic_data.isNullOrEmpty()) {
//            val localMedia = LocalMedia()
////            localMedia.path = HOLE_HOST_ADDRESS + "services/pkuhole/images/" + url
//            val uri = holeItem.pic_data!!.split(',')[1]
//            localMedia.path = uri
//            localMedia.setPosition(0)
//            val selectList: ArrayList<LocalMedia> = ArrayList(1)
//            selectList.add(localMedia)
//            PictureSelector.create(this)
//                .themeStyle(R.style.picture_WeChat_style)
//                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                .isNotPreviewDownload(true)
//                .imageEngine(GlideEngine.createGlideEngine())
//                .openExternalPreview(0, selectList)
            findNavController().navigate(NavigationDirections.actionGlobalNavPreviewPicture(holeItem.pid))
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