package cn.edu.pku.pkuhole.workers

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cn.edu.pku.pkuhole.data.hole.AppDatabase
import cn.edu.pku.pkuhole.data.hole.HoleListItemBean
import cn.edu.pku.pkuhole.data.hole.asDatabaseBean
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/24
 * @Desc:           暂时用不到了
 * @Version:        1.0
 */
// 预填充数据库
class HoleDatabaseWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    companion object {
        private const val TAG = "HoleDatabaseWorker"
        const val KEY_FILENAME = "PRE_POPULATE_HOLE_LIST_DATA"
    }

    @SuppressLint("TimberArgCount")
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val fileName = inputData.getString(KEY_FILENAME)
            if (fileName != null) {
                applicationContext.assets.open(fileName).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val holeType = object : TypeToken<List<HoleListItemBean>>() {}.type
                        val holeListList: List<HoleListItemBean> =
                            Gson().fromJson(jsonReader, holeType)
                        val database = AppDatabase.getInstance(applicationContext)
                        val result = holeListList.map { it.asDatabaseBean() }
                        database.holeListDao().insertAll(result)
                        Result.success()
                    }
                }
            } else {
                Timber.e("Error seeding database - no valid filename")
                Result.failure()
            }
        } catch (ex: Exception) {
            Timber.e(ex, "Error seeding database%s")
            Result.failure()
        }
    }
}