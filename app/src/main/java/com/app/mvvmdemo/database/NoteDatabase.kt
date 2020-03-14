package com.app.mvvmdemo.database

import android.content.Context
import android.os.AsyncTask
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app.mvvmdemo.model.Note


@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        private var instance: NoteDatabase? = null

        @Synchronized
        fun getInstance(context: Context): NoteDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java, "note_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(mRoomCallback)
                    .build()
            }
            return instance as NoteDatabase
        }

        private val mRoomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(@NonNull db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(instance).execute()
            }
        }

        private class PopulateDbAsyncTask(val db: NoteDatabase?) :
            AsyncTask<Void, Void, Void>() {
            private var noteDao: NoteDao? = null

            init {
                noteDao = db?.noteDao()
            }

            override fun doInBackground(vararg p0: Void?): Void? {
                return null
            }

        }
    }
}