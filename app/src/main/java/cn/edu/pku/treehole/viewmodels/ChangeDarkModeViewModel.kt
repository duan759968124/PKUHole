package cn.edu.pku.treehole.viewmodels

import androidx.lifecycle.MutableLiveData
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.hole.HoleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeDarkModeViewModel @Inject constructor(holeRepository: HoleRepository) :
    BaseViewModel(holeRepository = holeRepository) {

    var maxContentSize = LocalRepository.localHoleContentMaxTextSize
    var minContentSize = LocalRepository.localHoleContentMinTextSize
    var currentContentSize = MutableLiveData<Int?>().apply { value = LocalRepository.localGlobalHoleContentCurrentTextSize }


    fun onValueChanged(value: Float) {
        currentContentSize.value = value.toInt()
    }

    fun finishSetTextSize(){
//        localHoleContentCurrentTextSize 与 globalCurrentContentSize本质一样
//        currentContentSize 本质表示当面界面的字体大小
        LocalRepository.localGlobalHoleContentCurrentTextSize = currentContentSize.value!!
//        globalCurrentContentSize.value = currentContentSize.value!!
    }
}