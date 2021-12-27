package cn.edu.pku.pkuhole

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/21
 * @Desc:
 * @Version:        1.0
 */

@HiltAndroidApp
class HoleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//       init Timber
        Timber.plant(Timber.DebugTree())
    }
}