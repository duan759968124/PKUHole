package cn.edu.pku.treehole

import android.app.Application
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


/**
 *
 * @Time:           2021/12/21
 * @Desc:
 * @Version:        1.0
 */

@HiltAndroidApp
class HoleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//       init MMKV
        val rootDir = MMKV.initialize(this)
//        println("mmkv root: $rootDir")
        Timber.plant(Timber.DebugTree())
    }
}