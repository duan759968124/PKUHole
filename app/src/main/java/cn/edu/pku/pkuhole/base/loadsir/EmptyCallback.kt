package cn.edu.pku.pkuhole.base.loadsir

import android.content.Context
import android.view.View
import cn.edu.pku.pkuhole.R
import com.kingja.loadsir.callback.Callback

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/29
 * @Desc:
 * @Version:        1.0
 */
class EmptyCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.base_layout_empty
    }

    //当前Callback的点击事件，如果返回true则覆盖注册时的onReload()，如果返回false则两者都执行，先执行onReloadEvent()。
    override fun onReloadEvent(
        context: Context,
        view: View
    ): Boolean {
        return false
    }
}