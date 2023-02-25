package cn.edu.pku.treehole

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import cn.edu.pku.treehole.data.LocalRepository
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
        //根据app上次退出的状态来判断是否需要设置夜间模式,提前在SharedPreference中存了一个是否是夜间模式的boolean值
        // 如果字段是0-跟随系统 1-日间模式 2-夜间模式
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