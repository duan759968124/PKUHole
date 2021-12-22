package cn.edu.pku.pkuhole.data.hole

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/22
 * @Desc:
 * @Version:        1.0
 */
@Database(
    entities = [HoleAllListItemBean::class, HoleAttentionItemBean::class],
    version = 1,
    exportSchema = false
)
abstract class HoleDatabase : RoomDatabase() {

    abstract val holeAllListDao: HoleAllListDao
    abstract val holeAttentionDao: HoleAttentionDao

    companion object {
        @Volatile
        private var INSTANCE: HoleDatabase? = null
        fun getInstance(context: Context): HoleDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HoleDatabase::class.java,
                        "hole_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}