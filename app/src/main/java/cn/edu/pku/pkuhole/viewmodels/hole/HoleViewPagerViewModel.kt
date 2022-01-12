package cn.edu.pku.pkuhole.viewmodels.hole

import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.edu.pku.pkuhole.base.BaseViewModel
import cn.edu.pku.pkuhole.base.network.ApiException
import cn.edu.pku.pkuhole.data.hole.HoleRepository
import cn.edu.pku.pkuhole.utilities.ImageUtils
import cn.edu.pku.pkuhole.utilities.SingleLiveData
import com.luck.picture.lib.entity.LocalMedia
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.lang.Exception
import javax.inject.Inject
import com.luck.picture.lib.tools.SdkVersionUtils


/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2022/1/2
 * @Desc:
 * @Version:        1.0
 */
@HiltViewModel
class HoleViewPagerViewModel @Inject constructor(
    holeRepository: HoleRepository) : BaseViewModel(holeRepository = holeRepository) {

    val showDialogPost = SingleLiveData<Boolean>()


    private val _openPictureSelect = MutableLiveData<Boolean>(false)
    val openPictureSelect: LiveData<Boolean>
        get() = _openPictureSelect


    val postResponseMsg = SingleLiveData<String?>()


    // 发帖之后要涉及到更新hole数据和关注数据【自动关注的】
    init {

    }

    fun onClickUploadFab(){
        Timber.e("start dialog post hole!!!")
        showDialogPost.value = true
    }

    var postTextContent = MutableLiveData<String?>()
    var localPicFile = MutableLiveData<File?>()
    var localPicBase64 = MutableLiveData<String?>()

    fun postNewHole(){
        // Todo: 是否为空检查【图片和文本 】
        showDialogPost.value = false
        postResponseMsg.value = null
        if(localPicBase64.value.isNullOrEmpty() and postTextContent.value.isNullOrEmpty()){
            postResponseMsg.value = "输入内容为空"
        }else{
            Timber.e("click post new hole to server %s", postTextContent.value)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val token = getValidTokenWithFlow().singleOrNull()
                    token?.let {
                        val response = if(localPicBase64.value.isNullOrEmpty()){
                            database.postHoleOnlyText(token = it, text = postTextContent.value!!)
                        }else{
                            database.postHoleWithImage(token = it, text = postTextContent.value?:"",data=localPicBase64.value!!)
                        }
                        Timber.e("reply result %s", response.toString())
                        postResponseMsg.postValue("发布成功")
                    }
                }catch (e: Exception){
                    when(e){
                        is ApiException -> handleHoleFailResponse(e)
                        else -> errorStatus.postValue(e)
                    }
                }finally {
                    postTextContent.postValue("")
                    localPicFile.postValue(null)
                }
            }
        }
    }

    fun getLocalPicture(){
        _openPictureSelect.value = true
    }

    fun finishSelectPicture(localMedia: LocalMedia) {
        _openPictureSelect.value = false
        var file: File? = File(localMedia.path)
        if (SdkVersionUtils.isQ()) { //android Q路径变
            file = File(localMedia.androidQToPath)
        }
        localPicFile.value = file
        localPicBase64.value = file?.let { ImageUtils.encodeImage(it) }
    }

    fun clearContent() {
        postTextContent.value = ""
        localPicFile.value = null
        localPicBase64.value = null
    }

}