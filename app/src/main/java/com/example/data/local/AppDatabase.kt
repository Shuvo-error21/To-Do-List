package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.local.dao.*
import com.example.data.local.model.*

class AppTypeConverters {
    @TypeConverter
    fun fromGoalCategory(value: GoalCategory): String = value.name

    @TypeConverter
    fun toGoalCategory(value: String): GoalCategory = runCatching { GoalCategory.valueOf(value) }.getOrDefault(GoalCategory.STUDY)

    @TypeConverter
    fun fromPriorityLevel(value: PriorityLevel): String = value.name

    @TypeConverter
    fun toPriorityLevel(value: String): PriorityLevel = runCatching { PriorityLevel.valueOf(value) }.getOrDefault(PriorityLevel.MEDIUM)

    @TypeConverter
    fun fromAmbitionLevel(value: AmbitionLevel): String = value.name

    @TypeConverter
    fun toAmbitionLevel(value: String): AmbitionLevel = runCatching { AmbitionLevel.valueOf(value) }.getOrDefault(AmbitionLevel.MODERATE)
}

@Database(
    entities = [
        UserProfileEntity::class,
        GoalEntity::class,
        MilestoneEntity::class,
        TaskEntity::class,
        AiLogEntity::class,
        FocusSessionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao
    abstract fun aiLogDao(): AiLogDao
    abstract fun focusSessionDao(): FocusSessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "goal_ai_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
