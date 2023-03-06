package cn.edu.pku.treehole.viewmodels

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.hole.HoleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SetQuoteCommentViewModel @Inject constructor(holeRepository: HoleRepository) :
    BaseViewModel(holeRepository = holeRepository) {

    var currentSetQuote = MutableLiveData<Boolean?>().apply { value = LocalRepository.localSetQuote }


    fun onCheckedChanged(checked: Boolean) {
        currentSetQuote.value = checked
        LocalRepository.localSetQuote = checked
    }

}