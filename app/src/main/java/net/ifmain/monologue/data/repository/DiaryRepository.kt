package net.ifmain.monologue.data.repository

import kotlinx.coroutines.flow.Flow
import net.ifmain.monologue.data.api.DiaryApi
import net.ifmain.monologue.data.dao.DiaryDao
import net.ifmain.monologue.data.model.DiaryEntry
import net.ifmain.monologue.data.model.DiaryEntryDto
import javax.inject.Inject

class DiaryRepository @Inject constructor(
    private val dao: DiaryDao,
    private val api: DiaryApi
) {
    fun getEntries(): Flow<List<DiaryEntry>> = dao.getAll()

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
}
