package com.jonwal.dancetempo

import com.jonwal.dancetempo.data.local.LibraryBackupHelper
import com.jonwal.dancetempo.model.DanceRepository
import com.jonwal.dancetempo.model.Song
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LibraryBackupHelperTest {

    @Test
    fun testJsonExportAndParseRoundtrip() {
        val chaCha = DanceRepository.allDances.first { it.id == "cha_cha" }
        val discofox = DanceRepository.allDances.first { it.id == "discofox" }

        val testSongs = listOf(
            Song(
                id = 1,
                title = "Sway",
                artist = "Michael Bublé",
                bpm = 124,
                meter = 4,
                notes = "Toller Cha-Cha",
                streamingUrl = "https://open.spotify.com/track/123",
                dances = listOf(chaCha, discofox)
            )
        )

        val json = LibraryBackupHelper.exportToJson(testSongs)
        assertTrue(json.contains("Sway"))
        assertTrue(json.contains("cha_cha"))
        assertTrue(json.contains("discofox"))

        val parsed = LibraryBackupHelper.parseJson(json)
        assertEquals(1, parsed.size)
        val (song, danceIds) = parsed.first()
        assertEquals("Sway", song.title)
        assertEquals("Michael Bublé", song.artist)
        assertEquals(124, song.bpm)
        assertEquals(4, song.meter)
        assertEquals("Toller Cha-Cha", song.notes)
        assertEquals("https://open.spotify.com/track/123", song.streamingUrl)
        assertTrue(danceIds.contains("cha_cha"))
        assertTrue(danceIds.contains("discofox"))
    }

    @Test
    fun testCsvExportFormat() {
        val slowWaltz = DanceRepository.allDances.first { it.id == "slow_waltz" }
        val testSongs = listOf(
            Song(
                id = 2,
                title = "Come Away With Me",
                artist = "Norah Jones",
                bpm = 84,
                meter = 3,
                notes = "Schöner Walzer",
                dances = listOf(slowWaltz)
            )
        )

        val csv = LibraryBackupHelper.exportToCsv(testSongs)
        assertTrue(csv.startsWith("Titel;Interpret;BPM;Taktart;Tänze;Notiz;Streaming-Link"))
        assertTrue(csv.contains("\"Come Away With Me\""))
        assertTrue(csv.contains("84"))
    }
}
