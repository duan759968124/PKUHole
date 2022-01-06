package cn.edu.pku.pkuhole.viewmodels

import androidx.lifecycle.ViewModel
import cn.edu.pku.pkuhole.base.BaseViewModel
import cn.edu.pku.pkuhole.data.hole.HoleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(holeRepository: HoleRepository) :
    BaseViewModel(holeRepository = holeRepository) {
    // TODO: Implement the ViewModel
}