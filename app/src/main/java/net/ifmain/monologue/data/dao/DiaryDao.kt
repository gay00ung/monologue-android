package net.ifmain.monologue.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import net.ifmain.monologue.data.model.DiaryEntry

@Dao
interface DiaryDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(entry: DiaryEntry)

    @Update
    suspend fun update(entry: DiaryEntry)

    @Query("SELECT * FROM diary_entries ORDER BY date DESC")
    fun getAll(): Flow<List<DiaryEntry>>

    @Query("SELECT * FROM diary_entries WHERE userId = :userId ORDER BY date DESC")
    fun getByUser(userId: String): Flow<List<DiaryEntry>>

    @Query("UPDATE diary_entries SET isSynced = 1 WHERE date = :date")
    suspend fun markAsSynced(date: String)

    @Query("SELECT * FROM diary_entries WHERE isSynced = 0 AND userId = :userId")
    fun getUnsynced(userId: String): List<DiaryEntry>

    @androidx.room.Transaction
    suspend fun insertAndMarkSynced(entry: DiaryEntry) {
        insert(entry)
        markAsSynced(entry.date)
    }
}