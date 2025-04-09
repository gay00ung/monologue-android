package net.ifmain.monologue.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: DiaryEntry)

    @Query("SELECT * FROM diary_entries ORDER BY date DESC")
    fun getAll(): Flow<List<DiaryEntry>>
}
