package com.jonwal.dancetempo.model

data class Song(
    val id: Long = 0,
    val title: String,
    val artist: String,
    val bpm: Int,
    val meter: Int,
    val rating: Int, // 1 to 5
    val notes: String? = null,
    val streamingUrl: String? = null,
    val dances: List<Dance> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) {
    fun getTempoStatus(preferredDanceId: String? = null): TempoStatus {
        val targetDance = (if (preferredDanceId != null) {
            dances.find { it.id == preferredDanceId }
        } else null) ?: dances.firstOrNull() ?: return TempoStatus.IDEAL

        var effBpm = bpm
        if (targetDance.canHalfTap && effBpm < 140 && effBpm * 2 in targetDance.bpmScaleMin..targetDance.bpmScaleMax) {
            effBpm *= 2
        } else if (targetDance.halfBpmMin != null && effBpm in 55..75 && effBpm * 2 in targetDance.bpmScaleMin..targetDance.bpmScaleMax) {
            effBpm *= 2
        }

        return when {
            effBpm < targetDance.idealBpmMin -> TempoStatus.SLOW
            effBpm > targetDance.idealBpmMax -> TempoStatus.FAST
            else -> TempoStatus.IDEAL
        }
    }
}

enum class SongSortOrder(val displayName: String) {
    RATING_DESC("Beste Bewertung"),
    BPM_ASC("Tempo (BPM ⬆)"),
    BPM_DESC("Tempo (BPM ⬇)"),
    NEWEST_FIRST("Zuletzt hinzugefügt")
}
