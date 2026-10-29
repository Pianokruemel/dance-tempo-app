package com.jonwal.dancetempo

import com.jonwal.dancetempo.logic.TempoAnalyzer
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
    fun testQuickstepPopularSongs() {
        // 1. Honey, I'm Good (122 BPM) is ideal in dance school quickstep
        val matches122 = TempoAnalyzer.findMatches(bpm = 122, meter = 4)
        val qs122 = matches122.find { it.dance.id == "quickstep" }
        assertNotNull("Honey I'm Good (122 BPM) must match Quickstep", qs122)
        assertEquals(TempoStatus.IDEAL, qs122?.status)

        // 2. Marvin Gaye (110 BPM) is comfortable/slow quickstep
        val matches110 = TempoAnalyzer.findMatches(bpm = 110, meter = 4)
        val qs110 = matches110.find { it.dance.id == "quickstep" }
        assertNotNull("Marvin Gaye (110 BPM) must match Quickstep", qs110)
        assertEquals(TempoStatus.SLOW, qs110?.status)

        // 3. All About That Bass (134 BPM) is lively/fast quickstep
        val matches134 = TempoAnalyzer.findMatches(bpm = 134, meter = 4)
        val qs134 = matches134.find { it.dance.id == "quickstep" }
        assertNotNull("All About That Bass (134 BPM) must match Quickstep", qs134)
        assertEquals(TempoStatus.FAST, qs134?.status)
    }

    @Test
    fun testVienneseWaltz150Bpm() {
        // In social/dance school tempo, 150 BPM is comfortable entry
        val matches = TempoAnalyzer.findMatches(bpm = 150, meter = 3)
        val viennese = matches.find { it.dance.id == "viennese_waltz" }
        assertNotNull("Wiener Walzer should match 150 BPM", viennese)
        assertEquals(TempoStatus.IDEAL, viennese?.status)
    }

    @Test
    fun testMultipleMatchesAt124BpmHaveRhythmHints() {
        val matches = TempoAnalyzer.findMatches(bpm = 124, meter = 4)
        val names = matches.map { it.dance.name }

        assertTrue("Cha-Cha-Cha should match at 124 BPM", names.any { it.contains("Cha-Cha") })
        assertTrue("Discofox should match at 124 BPM", names.contains("Discofox"))
        assertTrue("Tango should match at 124 BPM", names.any { it.startsWith("Tango") })
        assertTrue("Quickstep should match at 124 BPM", names.any { it.startsWith("Quickstep") })
    }

    @Test
    fun testMismatchesIncludeExplanation() {
        val result = TempoAnalyzer.analyzeTempo(bpm = 124, meter = 4)
        val vienneseMismatch = result.mismatches.find { it.dance.id == "viennese_waltz" }
        val slowWaltzMismatch = result.mismatches.find { it.dance.id == "slow_waltz" }

        assertNotNull("Viennese Waltz should be in mismatches for 4/4 meter", vienneseMismatch)
        assertTrue("Viennese Waltz mismatch reason should mention meter", vienneseMismatch!!.isMeterMismatch)

        assertNotNull("Slow waltz should be in mismatches for 4/4 meter", slowWaltzMismatch)
        assertTrue("Slow waltz mismatch reason should mention meter", slowWaltzMismatch!!.isMeterMismatch)
    }
}
