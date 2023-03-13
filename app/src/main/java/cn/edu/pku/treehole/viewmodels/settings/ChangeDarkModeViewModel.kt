package cn.edu.pku.treehole.viewmodels.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import cn.edu.pku.treehole.base.BaseViewModel
import cn.edu.pku.treehole.data.LocalRepository
import cn.edu.pku.treehole.data.hole.HoleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeDarkModeViewModel @Inject constructor(holeRepository: HoleRepository) :
    BaseViewModel(holeRepository = holeRepository) {

    var currentDarkMode = MutableLiveData<Int?>().apply { value = LocalRepository.localDarkMode }


    fun onCheckedChanged(checked: Boolean) {
        if(checked){
            currentDarkMode.value = 0
        }else{
            if(LocalRepository.localUIDarkMode){
                currentDarkMode.value = 2
            }else{
                currentDarkMode.value = 1
            }

        }
    }

    fun clickNormalMode(){
        currentDarkMode.value = 1
    }

    fun clickDarkMode(){
        currentDarkMode.value = 2
    }

    fun finishChangeDarkMode(){
        LocalRepository.localDarkMode = currentDarkMode.value!!
        when(LocalRepository.localDarkMode){
            0->{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            1->{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            2->{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            }
        }
    }
}