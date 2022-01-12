package cn.edu.pku.pkuhole.utilities

import android.content.Context
import android.widget.Toast

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/29
 * @Desc:           暂时没用到
 * @Version:        1.0
 */
object ToastUtil {

    private var time: Long = 0
    private var oldMsg: String? = null

    fun show(msg: String) {
        if (msg != oldMsg) {
            create(msg)
            time = System.currentTimeMillis()
        } else {
            if (System.currentTimeMillis() - time > 2000) {
                create(msg)
                time = System.currentTimeMillis()
            }
        }
        oldMsg = msg
    }

    private fun create(massage: String) {
        val context: Context? = AppUtil.getApplication()?.applicationContext
        val toast = Toast(context)

        //设置显示时间
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }
}