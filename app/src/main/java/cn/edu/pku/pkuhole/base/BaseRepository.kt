package cn.edu.pku.pkuhole.base

import cn.edu.pku.pkuhole.api.DataState
import cn.edu.pku.pkuhole.api.HoleApiResponse
import cn.edu.pku.pkuhole.base.network.ApiException
import cn.edu.pku.pkuhole.base.network.ResultState
import cn.edu.pku.pkuhole.base.network.StateLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/28
 * @Desc:
 * @Version:        1.0
 */
open class BaseRepository {

    /**
     * 方式二：结合Flow请求数据。
     * 根据Flow的不同请求状态，如onStart、onEmpty、onCompletion等设置baseResp.dataState状态值，
     * 最后通过stateLiveData分发给UI层。
     *
     * @param block api的请求方法
     * @param stateLiveData 每个请求传入相应的LiveData，主要负责网络状态的监听
     */
    suspend fun <T : Any> executeReqWithFlow(
        block: suspend () -> HoleApiResponse<T>,
        stateLiveData: StateLiveData<T>
    ) {
        var holeApiResponse = HoleApiResponse<T>()
        flow {
            val respResult = block.invoke()
            holeApiResponse = respResult
            Timber.d("executeReqWithFlow: $holeApiResponse")
            holeApiResponse.dataState = DataState.STATE_SUCCESS
            stateLiveData.postValue(holeApiResponse)
            emit(respResult)
        }
            .flowOn(Dispatchers.IO)
            .onStart {
                Timber.d("executeReqWithFlow:onStart")
                holeApiResponse.dataState = DataState.STATE_LOADING
                stateLiveData.postValue(holeApiResponse)
            }
            .onEmpty {
                Timber.d("executeReqWithFlow:onEmpty")
                holeApiResponse.dataState = DataState.STATE_EMPTY
                stateLiveData.postValue(holeApiResponse)
            }
            .catch { exception ->
                run {
                    Timber.d("executeReqWithFlow:code  ${holeApiResponse.code}")
                    exception.printStackTrace()
                    holeApiResponse.dataState = DataState.STATE_ERROR
                    holeApiResponse.error = exception
                    stateLiveData.postValue(holeApiResponse)
                }
            }
            .collect {
                Timber.d( "executeReqWithFlow: collect")
                stateLiveData.postValue(holeApiResponse)
            }


    }

    /**
     * 方式一
     * repo 请求数据的公共方法，
     * 在不同状态下先设置 baseResp.dataState的值，最后将dataState 的状态通知给UI
     * @param block api的请求方法
     * @param stateLiveData 每个请求传入相应的LiveData，主要负责网络状态的监听
     */
    suspend fun <T : Any> executeResp(
        block: suspend () -> HoleApiResponse<T>,
        stateLiveData: StateLiveData<T>
    ) : HoleApiResponse<T> {
        var holeApiResponse = HoleApiResponse<T>()
        try {
            holeApiResponse.dataState = DataState.STATE_LOADING
            //开始请求数据
            val invoke = block.invoke()
            //将结果复制给baseResp
            holeApiResponse = invoke
            if (holeApiResponse.code == 0) {
                //请求成功，判断数据是否为空，
                //因为数据有多种类型，需要自己设置类型进行判断
                if (holeApiResponse.data == null || holeApiResponse.data is List<*> && (holeApiResponse.data as List<*>).size == 0) {
                    //TODO: 数据为空,结构变化时需要修改判空条件
                    holeApiResponse.dataState = DataState.STATE_EMPTY
                } else {
                    //请求成功并且数据为空的情况下，为STATE_SUCCESS
                    holeApiResponse.dataState = DataState.STATE_SUCCESS
                }

            } else {
                //服务器请求错误,分析不同的情况
                holeApiResponse.dataState = DataState.STATE_FAILED
            }
        } catch (e: Exception) {
            //非后台返回错误，捕获到的异常
            holeApiResponse.dataState = DataState.STATE_ERROR
            holeApiResponse.error = e
        } finally {
            stateLiveData.postValue(holeApiResponse)
        }
        return holeApiResponse
    }


    /**
     * @deprecated Use {@link executeResp} instead.
     */
    suspend fun <T : Any> executeResp(
        resp: HoleApiResponse<T>,
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): ResultState<T> {
        return coroutineScope {
            if (resp.code == 0) {
                successBlock?.let { it() }
                ResultState.Success(resp.data!!)
            } else {
                Timber.d( "executeResp: error")
                errorBlock?.let { it() }
                ResultState.Error(IOException(resp.msg))
            }
        }
    }









    /**
     * 带返回值的单个请求
     */
    suspend inline fun <reified T: Any> launchRequest(
        crossinline block : suspend () -> HoleApiResponse<T?>
    ): HoleApiResponse<T?> {
        return try {
            block()
        } catch (e: Exception){
            when(e){
                is UnknownHostException -> Timber.e(e.message)
                is ConnectException -> Timber.e(e.message)
                else -> Timber.e(e.message)
            }
            throw e
        }.run {
            if(code == 0){
                this
            }else{
                throw ApiException(code, msg)
            }
        }
    }






}