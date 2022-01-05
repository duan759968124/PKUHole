package cn.edu.pku.pkuhole

import android.app.Application
import cn.edu.pku.pkuhole.base.loadsir.EmptyCallback
import cn.edu.pku.pkuhole.base.loadsir.ErrorCallback
import cn.edu.pku.pkuhole.base.loadsir.LoadingCallback
import com.kingja.loadsir.core.LoadSir
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import com.tencent.mmkv.MMKV




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
//       init MMKV
        val rootDir = MMKV.initialize(this)
//        println("mmkv root: $rootDir")
//       init Timber
        Timber.plant(Timber.DebugTree())
        initLoadSir()
    }

    private fun initLoadSir() {
        LoadSir.beginBuilder()
            .addCallback(ErrorCallback())
            .addCallback(LoadingCallback())
            .addCallback(EmptyCallback())
            .setDefaultCallback(LoadingCallback::class.java)
            .commit()
    }
}