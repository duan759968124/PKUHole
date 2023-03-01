package cn.edu.pku.treehole.data.hole

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.edu.pku.treehole.utilities.DATABASE_NAME

/**
 *
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */
@Database(
//    entities = [HoleListItemBean::class, AttentionItemBean::class],
    entities = [TagBean::class, HoleItemBean::class, CommentItemBean::class],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tagDao(): TagDao
    abstract fun holeListDao(): HoleListDao
    abstract fun commentDao(): CommentDao

//    abstract fun attentionDao(): AttentionDao

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
                .fallbackToDestructiveMigration()
//                .addCallback(
//                    object : RoomDatabase.Callback() {
//                        override fun onCreate(db: SupportSQLiteDatabase) {
//                            super.onCreate(db)
//                            val request = OneTimeWorkRequestBuilder<HoleDatabaseWorker>()
//                                .setInputData(workDataOf(KEY_FILENAME to PRE_POPULATE_HOLE_LIST_DATA))
//                                .build()
//                            WorkManager.getInstance(context).enqueue(request)
//                        }
//                    }
//                )
                .build()
        }
    }
}