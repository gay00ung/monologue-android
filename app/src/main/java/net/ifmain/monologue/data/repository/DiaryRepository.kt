package net.ifmain.monologue.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import net.ifmain.monologue.data.api.DiaryApi
import net.ifmain.monologue.data.dao.DiaryDao
import net.ifmain.monologue.data.model.DiaryEntry
import net.ifmain.monologue.data.model.DiaryEntryDto
import java.time.LocalDate
import javax.inject.Inject

class DiaryRepository @Inject constructor(
    private val dao: DiaryDao,
    internal val api: DiaryApi
) {
    fun getEntries(userId: String): Flow<List<DiaryEntry>> = dao.getByUser(userId)

    suspend fun saveEntry(entry: DiaryEntry, userId: String) {
        dao.insert(entry)
        try {
            val dto = DiaryEntryDto(
                date = entry.date,
                text = entry.text,
                mood = entry.mood,
                userId = userId
            )
            val response = api.postDiary(dto)
            if (response.isSuccessful) {
                dao.markAsSynced(entry.date)
                Log.d("DiaryRepository", "Diary saved and marked as synced.")
            } else {
                Log.e("DiaryRepository", "Failed to save diary: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("DiaryRepository", "Error saving diary", e)
        }
    }

    suspend fun updateEntry(entry: DiaryEntry, userId: String) {
        dao.insert(entry)
        try {
            val dto = DiaryEntryDto(
                date = entry.date,
                text = entry.text,
                mood = entry.mood,
                userId = userId
            )
            val response = api.updateDiary(dto)
            if (response.isSuccessful) {
                dao.markAsSynced(entry.date)
                Log.d("DiaryRepository", "Diary updated and marked as synced.")
            } else {
                Log.e("DiaryRepository", "Failed to update diary: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("DiaryRepository", "Error updating diary", e)
        }
    }

    suspend fun checkDiaryExists(userId: String): Boolean {
        val todayDate = LocalDate.now().toString()
        return try {
            val diaries = api.getDiaries(userId)
            diaries.any { it.date == todayDate }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun syncUnsyncedEntries(userId: String) {
        val unsyncedEntries = withContext(Dispatchers.IO) {
            dao.getUnsynced(userId)
        }
        for (entry in unsyncedEntries) {
            try {
                val dto = DiaryEntryDto(
                    date = entry.date,
                    text = entry.text,
                    mood = entry.mood,
                    userId = userId
                )
                val response = api.postDiary(dto)
                if (response.isSuccessful) {
                    dao.markAsSynced(entry.date)
                    Log.d("DiaryRepository", "Synced diary for date=${entry.date}")
                } else {
                    Log.e("DiaryRepository", "Failed to sync diary: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("DiaryRepository", "Error syncing diary for date=${entry.date}", e)
            }
        }
    }

    suspend fun syncFromServer(userId: String) {
        try {
            val remoteEntries = api.getDiaries(userId)
            Log.d("syncFromServer", "Fetched ${remoteEntries.size} entries from server")
            Log.d("syncFromServer", "Server response: $remoteEntries")
            val entries = remoteEntries.map { dto ->
                Log.d("syncFromServer", "Received entry with userId=${dto.userId}, date=${dto.date}, text=${dto.text}, mood=${dto.mood}")
                DiaryEntry(
                    date = dto.date,
                    userId = dto.userId,
                    text = dto.text,
                    mood = dto.mood,
                    isSynced = true
                )
            }

            entries.forEach { entry ->
                dao.insert(entry)
                Log.d("syncFromServer", "Inserted entry for date=${entry.date}")
            }
        } catch (e: Exception) {
            Log.e("syncFromServer", "Error syncing from server", e)
        }
    }
}
