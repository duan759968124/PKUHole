package cn.edu.pku.pkuhole.data.hole

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import cn.edu.pku.pkuhole.utilities.DATABASE_NAME
import cn.edu.pku.pkuhole.utilities.PRE_POPULATE_HOLE_LIST_DATA
import cn.edu.pku.pkuhole.workers.HoleDatabaseWorker
import cn.edu.pku.pkuhole.workers.HoleDatabaseWorker.Companion.KEY_FILENAME

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */
@Database(
    entities = [HoleAllListItemBean::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val holeAllListDao: HoleAllListDao
//    abstract val holeAttentionDao: HoleAttentionDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
//            synchronized(this) {
//                var instance = INSTANCE
//                if (instance == null) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        AppDatabase::class.java,
//                        "pku_hole_db"
//                    )
//                        .fallbackToDestructiveMigration()
//                        .build()
//                    INSTANCE = instance
//                }
//                return instance
//            }
        }

        // Create and pre-populate the database. See this article for more details:
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
//                .fallbackToDestructiveMigration()
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<HoleDatabaseWorker>()
                                .setInputData(workDataOf(KEY_FILENAME to PRE_POPULATE_HOLE_LIST_DATA))
                                .build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                .build()
        }
    }
}