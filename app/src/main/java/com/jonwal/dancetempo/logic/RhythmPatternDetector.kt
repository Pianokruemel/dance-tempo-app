package com.jonwal.dancetempo.logic

data class RhythmPattern(
    val id: String,
    val displayName: String,
    val description: String,
    val ratios: List<Double>,
    val matchingDanceIds: List<String>
)

data class DetectedRhythm(
    val pattern: RhythmPattern,
    val confidence: Float // 0.0 .. 1.0
)

object RhythmPatternDetector {

    val canonicalPatterns: List<RhythmPattern> = listOf(
        RhythmPattern(
            id = "foxtrott_ssqq",
            displayName = "Slow - Slow - Quick - Quick",
            description = "Klassischer Foxtrott-Grundrhythmus (2:2:1:1)",
            ratios = listOf(2.0, 2.0, 1.0, 1.0),
            matchingDanceIds = listOf("foxtrott", "quickstep")
        ),
        RhythmPattern(
            id = "slowfox_sqq",
            displayName = "Slow - Quick - Quick",
            description = "Fließender Slowfox-Swing (2:1:1)",
            ratios = listOf(2.0, 1.0, 1.0),
            matchingDanceIds = listOf("slow_foxtrot")
        ),
        RhythmPattern(
            id = "rumba_qqs",
            displayName = "Quick - Quick - Slow",
            description = "Rumba Schrittrhythmus auf 2, 3, 4-1 (1:1:2)",
            ratios = listOf(1.0, 1.0, 2.0),
            matchingDanceIds = listOf("rumba")
        ),
        RhythmPattern(
            id = "samba_bounce",
            displayName = "1 - a - 2 Bounce",
            description = "Federnder Samba-Synkopenrhythmus (1.5 : 0.5 : 1)",
            ratios = listOf(1.5, 0.5, 1.0),
            matchingDanceIds = listOf("samba")
        ),
        RhythmPattern(
            id = "tango_ssqqs",
            displayName = "Slow - Slow - Quick - Quick - Slow",
            description = "Marcato-Gehrhythmus im Tango (2:2:1:1:2)",
            ratios = listOf(2.0, 2.0, 1.0, 1.0, 2.0),
            matchingDanceIds = listOf("tango")
        ),
        RhythmPattern(
            id = "chacha_chasse",
            displayName = "2, 3, 4-und-1 (Cha-Cha-Cha)",
            description = "Synkopierter Chassé-Rhythmus (1 : 1 : 0.5 : 0.5 : 1)",
            ratios = listOf(1.0, 1.0, 0.5, 0.5, 1.0),
            matchingDanceIds = listOf("cha_cha")
        )
    )

    /**
     * Analyzes tap intervals and matches them against known dance rhythm ratios.
     */
    fun detectPattern(deltas: List<Long>): DetectedRhythm? {
        if (deltas.size < 3) return null

        var bestMatch: RhythmPattern? = null
        var lowestMse = Double.MAX_VALUE

        for (pattern in canonicalPatterns) {
            val template = pattern.ratios
            if (deltas.size < template.size) continue

            // Evaluate the most recent window matching template length
            val sub = deltas.takeLast(template.size).map { it.toDouble() }
            val subSum = sub.sum()
            if (subSum <= 0.0) continue

            // Straight regular beats should not trigger syncopated pattern detection
            val minDelta = sub.minOrNull() ?: 1.0
            val maxDelta = sub.maxOrNull() ?: 1.0
            if (maxDelta / minDelta < 1.35) continue

            val templateSum = template.sum()
            val scale = templateSum / subSum
            val scaledSub = sub.map { it * scale }

            // Mean Squared Error (MSE)
            var mse = 0.0
            for (i in template.indices) {
                val diff = scaledSub[i] - template[i]
                mse += diff * diff
            }
            mse /= template.size

            // Threshold for human finger tapping: MSE < 0.22
            if (mse < 0.22 && mse < lowestMse) {
                lowestMse = mse
                bestMatch = pattern
            }
        }

        return bestMatch?.let {
            val confidence = ((0.22 - lowestMse) / 0.22).toFloat().coerceIn(0.5f, 1.0f)
            DetectedRhythm(it, confidence)
        }
    }
}
