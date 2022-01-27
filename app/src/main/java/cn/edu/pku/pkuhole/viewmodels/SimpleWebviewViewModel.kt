package cn.edu.pku.pkuhole.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.*
import cn.edu.pku.pkuhole.adapters.HoleItemListener
import cn.edu.pku.pkuhole.base.BaseViewModel
import cn.edu.pku.pkuhole.base.network.ApiException
import cn.edu.pku.pkuhole.data.hole.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2022/01/27
 * @Desc:
 * @Version:        1.0
 */

class SimpleWebviewViewModel(savedStateHandle: SavedStateHandle){

    val moduleName : String = savedStateHandle.get<String>(WEBVIEW_MODULE_NAME)!!
    val moduleUrl : String = savedStateHandle.get<String>(WEBVIEW_MODULE_URL)!!



    private var _replyDialogToName = MutableLiveData<String?>()

    val replyDialogToName: LiveData<String?>
        get() = _replyDialogToName

    init {

    }


    companion object {
        private const val WEBVIEW_MODULE_NAME = "webview_module_name"
        private const val WEBVIEW_MODULE_URL = "webview_module_url"
    }


    val holeItemClickListener = HoleItemListener{
        _replyDialogToName.value = ""
    }

    fun onCommentItemClicked(commentItem: CommentItemBean) {
        // 弹框
        _replyDialogToName.value = commentItem.name
    }


//    fun onCommentDialogFinished() {
//        // 弹框完成
//        _replyDialogToName.value = null
//    }



//    fun onReplyResponseFinished() {
//        _replyResponse.value = null
//    }

}