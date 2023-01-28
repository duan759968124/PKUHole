package cn.edu.pku.treehole.base.network

import androidx.lifecycle.MutableLiveData
import cn.edu.pku.treehole.api.HoleApiResponse

/**
 *
 * @Time:           2021/12/29
 * @Desc:
 * @Version:        1.0
 */
class StateLiveData<T> : MutableLiveData<HoleApiResponse<T>>()