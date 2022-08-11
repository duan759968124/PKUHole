package cn.edu.pku.treehole.viewmodels.hole

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.data.hole.HoleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PreviewPictureViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    holeRepository: HoleRepository
) : BaseViewModel(holeRepository = holeRepository) {


    companion object {
        private const val HOLE_ITEM_PID = "pid"
    }

    val pid : Long = savedStateHandle.get<Long>(HOLE_ITEM_PID)!!

    val currentHoleItem = database.getHoleItem(pid).asLiveData()

}
