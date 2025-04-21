package net.ifmain.monologue.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.ifmain.monologue.data.model.DiaryEntry

@Dao
interface DiaryDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(entry: DiaryEntry)

    @Query("SELECT * FROM diary_entries ORDER BY date DESC")
    fun getAll(): Flow<List<DiaryEntry>>

    @Query("UPDATE diary_entries SET isSynced = 1 WHERE date = :date")
    suspend fun markAsSynced(date: String)

    @Query("SELECT * FROM diary_entries WHERE isSynced = 0")
    fun getUnsynced(): List<DiaryEntry>
}