package cn.edu.pku.treehole.viewmodels.hole

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.base.network.ApiException
import cn.edu.pku.treehole.data.hole.HoleItemBean
import cn.edu.pku.treehole.data.hole.HoleRepository
import cn.edu.pku.treehole.data.hole.asDatabaseBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject internal constructor(
    savedStateHandle: SavedStateHandle,
    holeRepository: HoleRepository
) : BaseViewModel(holeRepository = holeRepository) {

    companion object {
        private const val SEARCH_KEY_WORDS = "keywords"
    }

    private val keywords : String = savedStateHandle.get<String>(SEARCH_KEY_WORDS)!!

    var searchList = MutableLiveData<List<HoleItemBean?>>().apply { value = arrayListOf() }

    private val _navigationToHoleItemDetail = MutableLiveData<Long?>()
    val navigationToHoleItemDetail: MutableLiveData<Long?>
        get() = _navigationToHoleItemDetail

    fun onHoleItemClicked(pid: Long) {
        _navigationToHoleItemDetail.value = pid
    }
    fun onHoleItemDetailNavigated() {
        _navigationToHoleItemDetail.value = null
    }

    var currentPage : Long = 0

    init {
        getSearchList()
    }


    @SuppressLint("TimberExceptionLogging")
    fun getSearchList(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadingStatus.postValue(true)
                val token = getValidTokenWithFlow().singleOrNull()
                token?.let { _ ->
                    val response =
                        if (keywords[0] == '#' && (keywords.length == 7 || keywords.length == 8)) {
                            database.searchPid(pid = keywords.substring(1))
                        } else {
                            database.search(keywords = keywords, page = currentPage + 1)
                        }
                    currentPage = response.data?.current_page ?: 1
                    val searchAllList =
                        searchList.value?.plus(response.data?.data!!.map {
                            if(!it.url.isNullOrEmpty()){
                                val pictureResponse =  database.getPictureData(pid = it.pid)
                                if(pictureResponse.code == 20000){
                                    it.pic_data = pictureResponse.data
                                }
                            }
                            it.asDatabaseBean()
                        })
                    searchList.postValue(searchAllList)
                }
            }catch (e: Exception){
                when(e){
                    is ApiException -> handleHoleFailResponse(e)
                    else -> errorStatus.postValue(e)
                }
            }finally {
                loadingStatus.postValue(false)
            }
        }
    }
}