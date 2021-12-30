package cn.edu.pku.pkuhole.base.network

import kotlin.Exception

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/28
 * @Desc:
 * @Version:        1.0
 */
// 没有用到在launchRequest中
sealed class ResultState<out T : Any> {
    data class Success<out T : Any>(val data: T) : ResultState<T>()
    data class Error(val exception: Exception) : ResultState<Nothing>()
}