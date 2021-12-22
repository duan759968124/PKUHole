package cn.edu.pku.pkuhole.viewmodels.hole

import android.app.Application
import androidx.lifecycle.ViewModel
import cn.edu.pku.pkuhole.data.hole.HoleAllListDao

class HoleAllListViewModel(dataSource: HoleAllListDao, application: Application) : ViewModel() {
    val database = dataSource
}