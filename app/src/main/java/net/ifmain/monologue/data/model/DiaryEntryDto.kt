package net.ifmain.monologue.data.model

data class DiaryEntryDto(
    val date: String,
    val text: String,
    val mood: String?,
    val userId: String
)
