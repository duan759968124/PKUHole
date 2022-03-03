package cn.edu.pku.pkuhole.viewmodels.hole

import android.annotation.SuppressLint
import androidx.lifecycle.*
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

@HiltViewModel
class SearchViewModel @Inject internal constructor(
    savedStateHandle: SavedStateHandle,
    holeRepository: HoleRepository
) : BaseViewModel(holeRepository = holeRepository) {

    companion object {
        private const val SEARCH_KEY_WORDS = "keywords"
    }

    private val keywords : String = savedStateHandle.get<String>(SEARCH_KEY_WORDS)!!

    var searchList = MutableLiveData<List<HoleItemBean?>>()

    private val _navigationToHoleItemDetail = MutableLiveData<Long?>()
    val navigationToHoleItemDetail: MutableLiveData<Long?>
        get() = _navigationToHoleItemDetail

    fun onHoleItemClicked(pid: Long) {
        _navigationToHoleItemDetail.value = pid
    }
    fun onHoleItemDetailNavigated() {
        _navigationToHoleItemDetail.value = null
    }

    init {
        getSearchList()
    }


    @SuppressLint("TimberExceptionLogging")
    fun getSearchList(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadingStatus.postValue(true)
                val token = getValidTokenWithFlow().singleOrNull()
                token?.let { token ->
                    val response = database.search(token=token, keywords = keywords)
                    searchList.postValue(response.data?.map { it.asDatabaseBean() })
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