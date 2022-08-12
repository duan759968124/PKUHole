package cn.edu.pku.treehole.viewmodels.hole

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.base.network.ApiException
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.hole.HoleRepository
import cn.edu.pku.treehole.utilities.ImageUtils
import cn.edu.pku.treehole.utilities.SingleLiveData
import com.luck.picture.lib.entity.LocalMedia
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject


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

    private val _getRandomTipFromNet = MutableLiveData<Boolean>().apply { value = false }
    val getRandomTipFromNet: LiveData<Boolean>
        get() = _getRandomTipFromNet


    val postResponseMsg = SingleLiveData<String?>()

    init {

        if(LocalRepository.getUid().isNotEmpty()){
            // 获取一条随机的树洞管理规范
            getRandomHoleManagementPractice()
            // 获取标签列表
//            getLabelList()
        }

    }

//    private fun getLabelList() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val token = getValidTokenWithFlow().singleOrNull()
//                token?.let {
//                    val response =
//                        database.()
//                    LocalRepository.localRandomTip = response.data?.desc ?: ""
//                    Timber.e("response tip: %s", response.data?.desc)
//                    Timber.e("localRandomTip tip: %s", LocalRepository.localRandomTip)
//                    _getRandomTipFromNet.postValue(true)
//                }
//            } catch (e: Exception) {
//                when (e) {
//                    is ApiException -> handleHoleFailResponse(e)
//                    else -> errorStatus.postValue(e)
//                }
//            } finally {
//            }
//        }
//    }

    private fun getRandomHoleManagementPractice() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val token = getValidTokenWithFlow().singleOrNull()
                token?.let {
                    val response =
                        database.getRandomHoleManagementPractice()
                    LocalRepository.localRandomTip = response.data?.desc ?: ""
                    Timber.e("response tip: %s", response.data?.desc)
                    Timber.e("localRandomTip tip: %s", LocalRepository.localRandomTip)
                    _getRandomTipFromNet.postValue(true)
                }
            } catch (e: Exception) {
                when (e) {
                    is ApiException -> handleHoleFailResponse(e)
                    else -> errorStatus.postValue(e)
                }
            } finally {
            }
        }

    }

    fun closeRandomTipDialog() {
        _getRandomTipFromNet.value = false
    }


    fun onClickUploadFab() {
        Timber.e("start dialog post hole!!!")
        showDialogPost.value = true
    }

    var postTextContent = MutableLiveData<String?>()
    var localPicFile = MutableLiveData<File?>()
    var localPicBase64 = MutableLiveData<String?>()

    fun postNewHole(){
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
                            database.postHoleOnlyText(text = postTextContent.value!!)
                        }else{
                            database.postHoleWithImage(text = postTextContent.value?:"",data=localPicBase64.value!!)
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
//        var file: File? = File(localMedia.path)
//        if (SdkVersionUtils.isQ()) { //android Q路径变
//            file = File(localMedia.androidQToPath)
//        }
//         使用压缩路径
        val file = File(localMedia.compressPath)
        localPicFile.value = file
        localPicBase64.value = file.let { "data:image/jpeg;base64," + ImageUtils.encodeImage(it) }
    }

    fun clearContent() {
        postTextContent.value = ""
        localPicFile.value = null
        localPicBase64.value = null
    }

}