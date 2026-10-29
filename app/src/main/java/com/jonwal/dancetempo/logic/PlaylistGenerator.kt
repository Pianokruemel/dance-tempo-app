package com.jonwal.dancetempo.logic

import com.jonwal.dancetempo.model.Song
import com.jonwal.dancetempo.model.TempoStatus

data class PlaylistItem(
    val song: Song,
    val position: Int,
    val assignedDanceName: String
)

data class GeneratedPlaylist(
    val items: List<PlaylistItem>,
    val totalEstimatedMinutes: Int,
    val danceCounts: Map<String, Int>
)

object PlaylistGenerator {

    private const val AVERAGE_SONG_DURATION_MINUTES = 3.5

    /**
     * Generates a balanced dance-party playlist from the user's library.
     *
     * @param songs All available songs in the library.
     * @param selectedDanceIds Filter dances to include. If empty, all dances are included.
     * @param durationMinutes Target duration in minutes (null = unlimited, include all matches).
     * @param avoidConsecutiveFast Avoid placing two fast dances (e.g. Quickstep + Jive) directly next to each other.
     */
    fun generate(
        songs: List<Song>,
        selectedDanceIds: Set<String>,
        durationMinutes: Int?,
        avoidConsecutiveFast: Boolean = true
    ): GeneratedPlaylist {
        // 1. Filter songs by selected dances
        val matchingSongs = if (selectedDanceIds.isEmpty()) {
            songs
        } else {
            songs.filter { song -> song.dances.any { selectedDanceIds.contains(it.id) } }
        }

        if (matchingSongs.isEmpty()) {
            return GeneratedPlaylist(emptyList(), 0, emptyMap())
        }

        // Calculate needed song count
        val targetCount = if (durationMinutes != null && durationMinutes > 0) {
            (durationMinutes / AVERAGE_SONG_DURATION_MINUTES).toInt().coerceAtLeast(1)
        } else {
            matchingSongs.size
        }

        // 2. Separate songs into slow, ideal, and fast pools for balanced rotation
        val shuffledPool = matchingSongs.shuffled().toMutableList()
        val result = mutableListOf<PlaylistItem>()
        val danceCounter = mutableMapOf<String, Int>()

        var lastWasFast = false
        var attempts = 0
        val maxAttempts = targetCount * 5

        while (result.size < targetCount && attempts < maxAttempts) {
            attempts++
            if (shuffledPool.isEmpty()) {
                // Refill if duration exceeds available unique songs
                shuffledPool.addAll(matchingSongs.shuffled())
            }

            val candidate = shuffledPool.removeAt(0)
            val dance = if (selectedDanceIds.isNotEmpty()) {
                candidate.dances.find { selectedDanceIds.contains(it.id) } ?: candidate.dances.firstOrNull()
            } else {
                candidate.dances.firstOrNull()
            }
            val danceName = dance?.name ?: "Tanz"

            val status = candidate.getTempoStatus(dance?.id)
            val isFast = status == TempoStatus.FAST

            if (avoidConsecutiveFast && isFast && lastWasFast && shuffledPool.isNotEmpty()) {
                // Defer fast song to avoid fatigue
                shuffledPool.add(candidate)
                continue
            }

            lastWasFast = isFast
            val item = PlaylistItem(
                song = candidate,
                position = result.size + 1,
                assignedDanceName = danceName
            )
            result.add(item)
            danceCounter[danceName] = (danceCounter[danceName] ?: 0) + 1
        }

        val totalMinutes = (result.size * AVERAGE_SONG_DURATION_MINUTES).toInt()
        return GeneratedPlaylist(
            items = result,
            totalEstimatedMinutes = totalMinutes,
            danceCounts = danceCounter
        )
    }

    /**
     * Exports a playlist as standard M3U format with metadata directives.
     */
    fun exportToM3u(playlist: GeneratedPlaylist): String {
        val sb = StringBuilder()
        sb.append("#EXTM3U\n")
        sb.append("#PLAYLIST:DanceTempo Tanz-Mix\n")
        playlist.items.forEach { item ->
            val durationSec = 210 // approx 3.5 min
            sb.append("#EXTINF:$durationSec,${item.song.artist} - ${item.song.title} [${item.assignedDanceName} - ${item.song.bpm} BPM]\n")
            sb.append("${item.song.streamingUrl ?: item.song.title}\n")
        }
        return sb.toString()
    }
}
