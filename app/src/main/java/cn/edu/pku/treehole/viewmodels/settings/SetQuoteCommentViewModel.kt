package cn.edu.pku.treehole.viewmodels.settings

import androidx.lifecycle.MutableLiveData
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.hole.HoleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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