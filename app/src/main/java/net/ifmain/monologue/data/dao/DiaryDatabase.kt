package net.ifmain.monologue.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import net.ifmain.monologue.data.model.DiaryEntry

@Database(entities = [DiaryEntry::class], version = 2)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao
}
