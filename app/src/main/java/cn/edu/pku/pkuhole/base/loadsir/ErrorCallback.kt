package cn.edu.pku.pkuhole.base.loadsir

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
class ErrorCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.base_layout_error
    }
}