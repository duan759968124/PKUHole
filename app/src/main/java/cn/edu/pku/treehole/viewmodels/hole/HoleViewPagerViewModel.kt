package cn.edu.pku.treehole.viewmodels.hole

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.base.network.ApiException
import cn.edu.pku.treehole.data.hole.HoleRepository
import cn.edu.pku.treehole.utilities.ImageUtils
import cn.edu.pku.treehole.utilities.SingleLiveData
import com.luck.picture.lib.entity.LocalMedia
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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
    holeRepository: HoleRepository,
) : BaseViewModel(holeRepository = holeRepository) {

    val showDialogPost = SingleLiveData<Boolean>()


    val tagNameList = database.getTagNameList().asLiveData()

    var tagTitle = "添加标签"
    var selectedTagName = ""

//    private val _openPictureSelect = MutableLiveData(false)
//    val openPictureSelect: LiveData<Boolean>
//        get() = _openPictureSelect
    val openPictureSelect = SingleLiveData<Boolean>()
//    private val _showTagListDialog = MutableLiveData(false)
//    val showTagListDialog: LiveData<Boolean>
//        get() = _showTagListDialog
    val showTagListDialog = SingleLiveData<Boolean>()


    val postResponseMsg = SingleLiveData<String?>()


    init {
    }


    fun onClickUploadFab() {
        Timber.e("start dialog post hole!!!")
        showDialogPost.value = true
    }

//    fun selectTagName(parent: AdapterView<*>?, view: View, pos: Int, id: Long){
////        Timber.e("select tag how to get name %d", selectedNamePosition)
//        Timber.e("select tag names %s", tagNameList.value)
//        Timber.e("select tag names %d %d", pos, id)
//        if(pos >= 0){
//            Timber.e("tag name %s", tagNameList.value?.get(pos) ?: "null")
//        }
//    }

    fun selectTagName() {
        showTagListDialog.value = true
    }

    fun dismissTagListDialog(tagName: String) {
        showTagListDialog.value = false
        selectedTagName = tagName
    }


    var postTextContent = MutableLiveData<String?>()
    var localPicFile = MutableLiveData<File?>()
    var localPicBase64 = MutableLiveData<String?>()

    fun postNewHole() {
        showDialogPost.value = false
        postResponseMsg.value = null
        if (localPicBase64.value.isNullOrEmpty() and postTextContent.value.isNullOrEmpty()) {
            postResponseMsg.value = "输入内容为空"
        } else {
            Timber.e("click post new hole to server %s", postTextContent.value)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val selectedTagId = database.getTagIdByName(selectedTagName).first()
                    val token = getValidTokenWithFlow().singleOrNull()
                    token?.let {
                        val response = if (localPicBase64.value.isNullOrEmpty()) {
                            database.postHoleOnlyText(text = postTextContent.value!!,
                                labelId = selectedTagId)
                        } else {
                            database.postHoleWithImage(text = postTextContent.value ?: "",
                                data = localPicBase64.value!!,
                                labelId = selectedTagId)
                        }
                        Timber.e("reply result %s", response.toString())
                        postResponseMsg.postValue("发布成功")
                    }
                } catch (e: Exception) {
                    when (e) {
                        is ApiException -> handleHoleFailResponse(e)
                        else -> errorStatus.postValue(e)
                    }
                } finally {
                    postTextContent.postValue("")
                    localPicFile.postValue(null)
                    selectedTagName = ""
                }
            }
        }
    }

    fun getLocalPicture() {
        openPictureSelect.value = true
    }

    fun finishSelectPicture(localMedia: LocalMedia) {
        openPictureSelect.value = false
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
        selectedTagName = ""
    }

}