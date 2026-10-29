package com.jonwal.dancetempo.logic

import com.jonwal.dancetempo.model.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

object TempoAnalyzer {

    const val TIMEOUT_RESET_MS = 2200L
    private const val MIN_TAPS_FOR_BPM = 3

    /**
     * Calculates BPM using the median interval of recent taps.
     */
    fun calculateBpm(timestamps: List<Long>): Int? {
        if (timestamps.size < MIN_TAPS_FOR_BPM) return null

        val deltas = mutableListOf<Long>()
        for (i in 1 until timestamps.size) {
            val delta = timestamps[i] - timestamps[i - 1]
            if (delta in 150..2500) { // Filter out accidental instant multi-touches
                deltas.add(delta)
            }
        }

        if (deltas.isEmpty()) return null

        deltas.sort()
        val medianDelta = deltas[deltas.size / 2]
        if (medianDelta == 0L) return null

        return (60000.0 / medianDelta).roundToInt()
    }

    /**
     * Infers meter (2, 3, 4) from the indices of designated downbeats ('1. Schlag').
     */
    fun detectMeter(downbeatIndices: List<Int>): Int? {
        if (downbeatIndices.size < 2) return null

        val barLengths = mutableListOf<Int>()
        for (i in 1 until downbeatIndices.size) {
            val diff = downbeatIndices[i] - downbeatIndices[i - 1]
            if (diff in 2..6) {
                barLengths.add(diff)
            }
        }

        if (barLengths.isEmpty()) return null

        return barLengths
            .groupingBy { it }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
    }

    /**
     * Complete evaluation producing both matching and non-matching dances with explanations.
     */
    fun analyzeTempo(
        bpm: Int,
        meter: Int? = null,
        dances: List<Dance> = DanceRepository.allDances
    ): EvaluationResult {
        val matches = mutableListOf<DanceMatch>()
        val mismatches = mutableListOf<DanceMismatch>()

        for (dance in dances) {
            // 1. Meter Compatibility Check
            var isMeterCompatible = true
            if (meter != null) {
                val matchesPrimary = (dance.meter == meter)
                val matchesAlt = (dance.altMeter != null && dance.altMeter == meter)
                val evenMeterCompat = (meter == 2 && dance.meter == 4) || (meter == 4 && dance.meter == 2)
                isMeterCompatible = matchesPrimary || matchesAlt || evenMeterCompat
            }

            // 2. Multi-Octave Tempo Matching
            var effectiveBpm = bpm
            var isHalfSpeed = false

            // Only double if dance supports half-tap AND measured BPM is under 100
            // (In hobby mode, Quickstep is 98-142 BPM, so 110-134 BPM is direct 1:1, not doubled!)
            if (dance.canHalfTap && bpm in 50..95 && (bpm * 2 in dance.bpmScaleMin..dance.bpmScaleMax)) {
                effectiveBpm = bpm * 2
                isHalfSpeed = true
            } else if (dance.halfBpmMin != null && bpm in 55..75 && (bpm * 2 in dance.bpmScaleMin..dance.bpmScaleMax)) {
                effectiveBpm = bpm * 2
            }

            val isInTempoRange = effectiveBpm in dance.bpmScaleMin..dance.bpmScaleMax

            if (isMeterCompatible && isInTempoRange) {
                // MATCH
                val rangeSpan = (dance.bpmScaleMax - dance.bpmScaleMin).toFloat()
                val currentPct = if (rangeSpan > 0) {
                    max(0f, min(1f, (effectiveBpm - dance.bpmScaleMin) / rangeSpan))
                } else 0.5f

                val idealStartPct = if (rangeSpan > 0) {
                    max(0f, min(1f, (dance.idealBpmMin - dance.bpmScaleMin) / rangeSpan))
                } else 0f

                val idealEndPct = if (rangeSpan > 0) {
                    max(0f, min(1f, (dance.idealBpmMax - dance.bpmScaleMin) / rangeSpan))
                } else 1f

                val status = when {
                    effectiveBpm < dance.idealBpmMin -> TempoStatus.SLOW
                    effectiveBpm > dance.idealBpmMax -> TempoStatus.FAST
                    else -> TempoStatus.IDEAL
                }

                val idealCenter = (dance.idealBpmMin + dance.idealBpmMax) / 2f
                val diffToCenter = abs(effectiveBpm - idealCenter)

                matches.add(
                    DanceMatch(
                        dance = dance,
                        effectiveBpm = effectiveBpm,
                        isHalfSpeed = isHalfSpeed,
                        percentage = currentPct,
                        status = status,
                        idealStartPercentage = idealStartPct,
                        idealEndPercentage = idealEndPct,
                        diffToCenter = diffToCenter
                    )
                )
            } else {
                // MISMATCH: Formulate clear, concise reason
                val reason: String
                val isTooSlow = effectiveBpm < dance.bpmScaleMin
                val isTooFast = effectiveBpm > dance.bpmScaleMax
                val bpmDiff = if (isTooSlow) {
                    dance.idealBpmMin - effectiveBpm
                } else if (isTooFast) {
                    effectiveBpm - dance.idealBpmMax
                } else null

                val targetNorm = "Tanzschul-Ideal"

                if (!isMeterCompatible && meter != null) {
                    if (isInTempoRange) {
                        reason = "Falscher Takt: Benötigt ${dance.meter}/4-Takt (du hast $meter/4 getappt). Tempo (${effectiveBpm} BPM) würde passen."
                    } else {
                        val tempoNote = if (isTooSlow) "zu langsam (${dance.idealBpmMin}–${dance.idealBpmMax} BPM ideal)" else "zu schnell (${dance.idealBpmMin}–${dance.idealBpmMax} BPM ideal)"
                        reason = "Falsche Taktart (${dance.meter}/4 statt $meter/4) und $tempoNote."
                    }
                } else {
                    reason = if (isTooSlow) {
                        "Zu langsam: Mindestens ${dance.bpmScaleMin} BPM nötig ($targetNorm: ${dance.idealBpmMin}–${dance.idealBpmMax} BPM, Δ -${bpmDiff} BPM)."
                    } else {
                        "Zu schnell: Maximal ca. ${dance.bpmScaleMax} BPM ($targetNorm: ${dance.idealBpmMin}–${dance.idealBpmMax} BPM, Δ +${bpmDiff} BPM)."
                    }
                }

                mismatches.add(
                    DanceMismatch(
                        dance = dance,
                        reason = reason,
                        isMeterMismatch = !isMeterCompatible,
                        isTooSlow = isTooSlow,
                        isTooFast = isTooFast,
                        bpmDifference = bpmDiff
                    )
                )
            }
        }

        return EvaluationResult(
            matches = matches.sortedBy { it.diffToCenter },
            mismatches = mismatches.sortedWith(
                compareBy(
                    { it.isMeterMismatch }, // Meter mismatches last
                    { it.bpmDifference ?: 999 } // Closest non-matches first
                )
            )
        )
    }

    /**
     * Backward-compatible convenience function.
     */
    fun findMatches(
        bpm: Int,
        meter: Int? = null,
        dances: List<Dance> = DanceRepository.allDances
    ): List<DanceMatch> {
        return analyzeTempo(bpm, meter, dances).matches
    }
}
