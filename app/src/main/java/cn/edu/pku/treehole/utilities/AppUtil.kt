package cn.edu.pku.treehole.utilities

import android.os.Build
import cn.edu.pku.treehole.BuildConfig
import cn.edu.pku.treehole.data.LocalRepository

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/29
 * @Desc:
 * @Version:        1.0
 */
object AppUtil {
//    private var sApplication: Application? = null
//
//    @SuppressLint("PrivateApi")
//    fun getApplication(): Application? {
//        if (sApplication == null) {
//            try {
//                sApplication = Class.forName("android.app.ActivityThread")
//                    .getMethod("currentApplication")
//                    .invoke(null, null as Array<Any?>?) as Application
//            } catch (e: IllegalAccessException) {
//                e.printStackTrace()
//            } catch (e: InvocationTargetException) {
//                e.printStackTrace()
//            } catch (e: NoSuchMethodException) {
//                e.printStackTrace()
//            } catch (e: ClassNotFoundException) {
//                e.printStackTrace()
//            }
//        }
//        return sApplication
//    }

    // 安卓版本
    val deviceAndroidVersion: String
        get() = Build.VERSION.RELEASE


    // 型号
    val deviceModel: String
        get() {
            val model = Build.MODEL
            return if(model.isNullOrEmpty()){
                "unknown"
            } else{
                model.trim().replace("\\s*".toRegex(), "").replace("_".toRegex(), "-")
            }
        }

    // 品牌
    val deviceBrand: String
        get() {
            val brand = Build.BRAND
            return if(brand.isNullOrEmpty()){
                "unknown"
            } else{
                brand.trim().replace("\\s*".toRegex(), "").replace("_".toRegex(), "-")
            }
        }

    // app版本
    val appVersion: String
        get() = BuildConfig.VERSION_NAME

    // 包名
    val appPackage: String
        get() = BuildConfig.APPLICATION_ID


    fun getUserAgent(): String {
        return "Android_" + APP_NAME + "_" + appVersion + "_" + deviceModel + "Of" + deviceBrand +
                "_Android" + deviceAndroidVersion + "_" + LocalRepository.getAppInstallId()
    }

}