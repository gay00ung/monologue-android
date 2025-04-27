package net.ifmain.monologue.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
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
    fun getEntries(): Flow<List<DiaryEntry>> = dao.getAll()
    data class DiaryCheckResponse(val status: String)

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
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun checkDiaryExists(userId: String): Boolean {
        val todayDate = LocalDate.now().toString()
        try {
            val diaries = api.getDiaries(userId)
            return diaries.any { it.date == todayDate }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    suspend fun updateEntry(entry: DiaryEntry, userId: String) {
        try {
            val dto = DiaryEntryDto(
                date = entry.date,
                text = entry.text,
                mood = entry.mood,
                userId = userId
            )
            val response = api.updateDiary(dto)
            if (response.isSuccessful) {
                Log.d("DiaryRepository", "Diary entry updated successfully.")
            } else {
                Log.e("DiaryRepository", "Failed to update diary: ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
