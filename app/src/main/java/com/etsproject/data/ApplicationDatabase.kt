package com.etsproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(entities = [Profile::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao

    companion object {
        @Volatile
        private var instance: ApplicationDatabase? = null
        fun getApplicationDatabase(context: Context): ApplicationDatabase {
            instance?.let { return it }
            synchronized(this) {
                val temp: ApplicationDatabase = Room
                    .databaseBuilder(
                        context.applicationContext,
                        ApplicationDatabase::class.java, "room_database"
                    )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback())
                    .build()
                instance = temp
                return temp
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                instance?.let { database ->
                    CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
                        val profile = Profile(
                            0,
                            "Kemal",
                            "Demir",
                            BirthDate(12, 8, 1968),
                            "example@example.com",
                            PhoneNumber("+90", "555 555 55 55"),
                            null
                        )
                        database.profileDao().insertProfile(profile)
                        val profile2 = Profile(
                            1,
                            "Pınar",
                            "Özkan",
                            BirthDate(4, 11, 1993),
                            "example@example.com",
                            PhoneNumber("+90", "555 555 5555"),
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                    "Nam quis tellus vitae ante semper tempor."
                        )
                        database.profileDao().insertProfile(profile2)
                        val profile3 = Profile(
                            2,
                            "Deniz",
                            "Sümer",
                            BirthDate(24, 6, 2007),
                            "example@example.com",
                            PhoneNumber("+90", "555 555 5555"),
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                    "Nam quis tellus vitae ante semper tempor."
                        )
                        database.profileDao().insertProfile(profile3)
                    }
                }
            }
        }
    }
}