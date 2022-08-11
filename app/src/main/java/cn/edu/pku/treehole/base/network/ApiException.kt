package cn.edu.pku.treehole.base.network

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/29
 * @Desc:
 * @Version:        1.0
 */

class ApiException(val code: Int, var msg: String?) : Exception() {
    override val message: String?
        get() = msg
}