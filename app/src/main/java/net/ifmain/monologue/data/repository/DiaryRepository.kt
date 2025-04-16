package net.ifmain.monologue.data.repository

import kotlinx.coroutines.flow.Flow
import net.ifmain.monologue.data.api.DiaryApi
import net.ifmain.monologue.data.dao.DiaryDao
import net.ifmain.monologue.data.model.DiaryEntry
import net.ifmain.monologue.data.model.DiaryEntryDto

class DiaryRepository(
    private val dao: DiaryDao,
    private val api: DiaryApi
) {
    fun getEntries(): Flow<List<DiaryEntry>> = dao.getAll()

    suspend fun saveEntry(entry: DiaryEntry) {
        dao.insert(entry)
        try {
            val dto = DiaryEntryDto(
                date = entry.date,
                text = entry.text,
                mood = entry.mood,
                userId = "test-user"
            )
            api.postDiary(dto)
        } catch (e: Exception) {
            // 실패시 isSynced = false 로 저장
        }
    }

}
