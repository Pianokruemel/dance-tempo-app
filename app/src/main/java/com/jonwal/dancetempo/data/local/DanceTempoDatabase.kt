package com.jonwal.dancetempo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jonwal.dancetempo.data.local.entity.SongDanceCrossRef
import com.jonwal.dancetempo.data.local.entity.SongEntity

@Database(
    entities = [SongEntity::class, SongDanceCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class DanceTempoDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao

    companion object {
        @Volatile
        private var INSTANCE: DanceTempoDatabase? = null

        fun getInstance(context: Context): DanceTempoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DanceTempoDatabase::class.java,
                    "dance_tempo_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
