package cn.edu.pku.pkuhole.utilities

import android.annotation.SuppressLint
import android.app.Application
import java.lang.reflect.InvocationTargetException

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/29
 * @Desc:
 * @Version:        1.0
 */
object AppUtil {
    private var sApplication: Application? = null

    @SuppressLint("PrivateApi")
    fun getApplication(): Application? {
        if (sApplication == null) {
            try {
                sApplication = Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication")
                    .invoke(null, null as Array<Any?>?) as Application
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        }
        return sApplication
    }
}