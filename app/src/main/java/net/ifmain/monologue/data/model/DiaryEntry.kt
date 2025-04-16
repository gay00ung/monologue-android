package net.ifmain.monologue.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_entries")
data class DiaryEntry(
    @PrimaryKey val date: String,
    val text: String,
    val mood: String?,
    val isSynced: Boolean = false
)