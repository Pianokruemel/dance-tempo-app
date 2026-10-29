package com.jonwal.dancetempo

import com.jonwal.dancetempo.logic.TempoAnalyzer
import com.jonwal.dancetempo.model.DanceLevelMode
import com.jonwal.dancetempo.model.TempoStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TempoAnalyzerTest {

    @Test
    fun testBpmCalculation() {
        val timestamps = listOf(0L, 500L, 1000L, 1500L, 2000L)
        val bpm = TempoAnalyzer.calculateBpm(timestamps)
        assertEquals(120, bpm)
    }

    @Test
    fun testMeterDetectionWalzer3_4() {
        val downbeatIndices = listOf(0, 3, 6, 9)
        val meter = TempoAnalyzer.detectMeter(downbeatIndices)
        assertEquals(3, meter)
    }

    @Test
    fun testMeterDetection4_4() {
        val downbeatIndices = listOf(0, 4, 8, 12)
        val meter = TempoAnalyzer.detectMeter(downbeatIndices)
        assertEquals(4, meter)
    }

    @Test
    fun testHobbyModeQuickstepSongs() {
        // 1. Honey, I'm Good (122 BPM) should be IDEAL in Hobby mode
        val matches122 = TempoAnalyzer.findMatches(bpm = 122, meter = 4, mode = DanceLevelMode.HOBBY)
        val qs122 = matches122.find { it.dance.id == "quickstep" }
        assertNotNull("Honey I'm Good (122 BPM) must match Quickstep in Hobby mode", qs122)
        assertEquals(TempoStatus.IDEAL, qs122?.status)

        // 2. Marvin Gaye (110 BPM) should be SLOW (gemütlich) in Hobby mode
        val matches110 = TempoAnalyzer.findMatches(bpm = 110, meter = 4, mode = DanceLevelMode.HOBBY)
        val qs110 = matches110.find { it.dance.id == "quickstep" }
        assertNotNull("Marvin Gaye (110 BPM) must match Quickstep in Hobby mode", qs110)
        assertEquals(TempoStatus.SLOW, qs110?.status)

        // 3. All About That Bass (134 BPM) should be FAST (spritzig / flott) in Hobby mode
        val matches134 = TempoAnalyzer.findMatches(bpm = 134, meter = 4, mode = DanceLevelMode.HOBBY)
        val qs134 = matches134.find { it.dance.id == "quickstep" }
        assertNotNull("All About That Bass (134 BPM) must match Quickstep in Hobby mode", qs134)
        assertEquals(TempoStatus.FAST, qs134?.status)
    }

    @Test
    fun testVienneseWaltzHobbyAndTournament() {
        // In Hobby mode, 150 BPM is a comfortable entry tempo for Viennese Waltz
        val hobbyMatches = TempoAnalyzer.findMatches(bpm = 150, meter = 3, mode = DanceLevelMode.HOBBY)
        val vienneseHobby = hobbyMatches.find { it.dance.id == "viennese_waltz" }
        assertNotNull("Wiener Walzer should match 150 BPM in Hobby mode", vienneseHobby)
        assertEquals(TempoStatus.IDEAL, vienneseHobby?.status)

        // In Tournament mode, 150 BPM is outside tournament boundaries (requires min 160, ideal 174-180 BPM)
        val tourneyResult = TempoAnalyzer.analyzeTempo(bpm = 150, meter = 3, mode = DanceLevelMode.TOURNAMENT)
        val vienneseMismatch = tourneyResult.mismatches.find { it.dance.id == "viennese_waltz" }
        assertNotNull("Wiener Walzer should be a mismatch at 150 BPM in Tournament mode", vienneseMismatch)
        assertTrue(vienneseMismatch!!.isTooSlow)
    }

    @Test
    fun testTournamentModeQuickstep204BpmIsIdeal() {
        val matches = TempoAnalyzer.findMatches(bpm = 204, meter = 4, mode = DanceLevelMode.TOURNAMENT)
        val quickstep = matches.find { it.dance.id == "quickstep" }
        assertNotNull("Quickstep should match 204 BPM in tournament mode", quickstep)
        assertEquals(TempoStatus.IDEAL, quickstep?.status)
    }

    @Test
    fun testMultipleMatchesAt124BpmHaveRhythmHints() {
        val matches = TempoAnalyzer.findMatches(bpm = 124, meter = 4, mode = DanceLevelMode.HOBBY)
        val names = matches.map { it.dance.name }
        
        assertTrue("Cha-Cha-Cha should match at 124 BPM", names.any { it.contains("Cha-Cha") })
        assertTrue("Discofox should match at 124 BPM", names.contains("Discofox"))
        assertTrue("Tango should match at 124 BPM", names.any { it.startsWith("Tango") })
        assertTrue("Quickstep should match at 124 BPM", names.any { it.startsWith("Quickstep") })
    }

    @Test
    fun testMismatchesIncludeExplanation() {
        val result = TempoAnalyzer.analyzeTempo(bpm = 124, meter = 4, mode = DanceLevelMode.TOURNAMENT)
        val vienneseMismatch = result.mismatches.find { it.dance.id == "viennese_waltz" }
        val jiveMismatch = result.mismatches.find { it.dance.id == "jive" }

        assertNotNull("Viennese Waltz should be in mismatches for 4/4 meter", vienneseMismatch)
        assertTrue("Viennese Waltz mismatch reason should mention meter", vienneseMismatch!!.isMeterMismatch)

        assertNotNull("Jive should be in mismatches at 124 BPM in tournament mode", jiveMismatch)
        assertTrue("Jive should be flagged as too slow at 124 BPM", jiveMismatch!!.isTooSlow)
        assertTrue(jiveMismatch.reason.contains("Zu langsam"))
    }
}
