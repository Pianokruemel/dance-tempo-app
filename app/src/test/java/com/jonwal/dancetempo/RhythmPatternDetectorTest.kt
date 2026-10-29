package com.jonwal.dancetempo

import com.jonwal.dancetempo.logic.RhythmPatternDetector
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class RhythmPatternDetectorTest {

    @Test
    fun testDetectSlowSlowQuickQuickFoxtrott() {
        // Slow-Slow-Quick-Quick: ~1000ms, ~980ms, ~510ms, ~490ms
        val deltas = listOf(1000L, 980L, 510L, 490L)
        val detected = RhythmPatternDetector.detectPattern(deltas)

        assertNotNull("Should detect Foxtrott SSQQ", detected)
        assertEquals("foxtrott_ssqq", detected?.pattern?.id)
    }

    @Test
    fun testDetectSlowQuickQuickSlowfox() {
        // Slow-Quick-Quick: ~1000ms, ~510ms, ~490ms
        val deltas = listOf(1000L, 510L, 490L)
        val detected = RhythmPatternDetector.detectPattern(deltas)

        assertNotNull("Should detect Slowfox SQQ", detected)
        assertEquals("slowfox_sqq", detected?.pattern?.id)
    }

    @Test
    fun testDetectSambaBounce() {
        // 1-a-2: ~750ms, ~250ms, ~500ms (1.5 : 0.5 : 1)
        val deltas = listOf(750L, 250L, 500L)
        val detected = RhythmPatternDetector.detectPattern(deltas)

        assertNotNull("Should detect Samba Bounce", detected)
        assertEquals("samba_bounce", detected?.pattern?.id)
    }

    @Test
    fun testRegularStraightBeatsYieldNoPattern() {
        // Straight 500ms beats (just regular quarter notes)
        val deltas = listOf(500L, 500L, 500L, 500L)
        val detected = RhythmPatternDetector.detectPattern(deltas)

        // Straight beats shouldn't falsely trigger syncopated patterns
        assertEquals(null, detected)
    }
}
