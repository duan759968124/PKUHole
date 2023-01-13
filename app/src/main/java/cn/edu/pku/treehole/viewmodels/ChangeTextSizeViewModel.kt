package cn.edu.pku.treehole.viewmodels

import androidx.lifecycle.MutableLiveData
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.hole.HoleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChangeTextSizeViewModel @Inject constructor(holeRepository: HoleRepository) :
    BaseViewModel(holeRepository = holeRepository) {

    var maxContentSize = LocalRepository.localHoleContentMaxTextSize
    var minContentSize = LocalRepository.localHoleContentMinTextSize
    var defaultContentSize = LocalRepository.localHoleContentDefaultTextSize
    var currentContentSize = MutableLiveData<Int?>().apply { value = LocalRepository.localHoleContentCurrentTextSize }

    fun onValueChanged(value: Float) {
        currentContentSize.value = value.toInt()
    }

    fun finishSetTextSize(){
        LocalRepository.localHoleContentDefaultTextSize = currentContentSize.value!!
    }
}