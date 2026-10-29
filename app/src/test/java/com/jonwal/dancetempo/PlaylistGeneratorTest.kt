package com.jonwal.dancetempo

import com.jonwal.dancetempo.logic.PlaylistGenerator
import com.jonwal.dancetempo.model.DanceRepository
import com.jonwal.dancetempo.model.Song
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlaylistGeneratorTest {

    @Test
    fun testPlaylistGenerationWithDuration() {
        val chaCha = DanceRepository.allDances.first { it.id == "cha_cha" }
        val quickstep = DanceRepository.allDances.first { it.id == "quickstep" }

        val testSongs = listOf(
            Song(id = 1, title = "Song A", artist = "Artist A", bpm = 120, meter = 4, dances = listOf(chaCha)),
            Song(id = 2, title = "Song B", artist = "Artist B", bpm = 122, meter = 4, dances = listOf(quickstep)),
            Song(id = 3, title = "Song C", artist = "Artist C", bpm = 124, meter = 4, dances = listOf(chaCha))
        )

        // Request approx 7 minutes (2 songs)
        val playlist = PlaylistGenerator.generate(
            songs = testSongs,
            selectedDanceIds = emptySet(),
            durationMinutes = 7,
            avoidConsecutiveFast = true
        )

        assertEquals(2, playlist.items.size)
        assertTrue(playlist.totalEstimatedMinutes in 6..8)

        // Test M3U export
        val m3u = PlaylistGenerator.exportToM3u(playlist)
        assertTrue(m3u.startsWith("#EXTM3U"))
        assertTrue(m3u.contains("#PLAYLIST:DanceTempo Tanz-Mix"))
    }
}
