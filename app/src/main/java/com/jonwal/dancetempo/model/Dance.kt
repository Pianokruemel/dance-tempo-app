package com.jonwal.dancetempo.model

enum class DanceCategory(val displayName: String) {
    STANDARD("Standard"),
    LATIN("Latein"),
    SOCIAL("Social")
}

enum class DanceLevelMode(val displayName: String) {
    HOBBY("Hobby"),
    TOURNAMENT("Turnier")
}

enum class TempoStatus(val hobbyLabel: String, val tournamentLabel: String) {
    SLOW("Gemütlich / Einsteiger", "Langsamer / Gemütlich"),
    IDEAL("Tanzschul-Ideal", "Turniertempo (Ideal)"),
    FAST("Spritzig / Flott", "Schnell / Sportlich");

    fun getLabel(mode: DanceLevelMode): String = when (mode) {
        DanceLevelMode.HOBBY -> hobbyLabel
        DanceLevelMode.TOURNAMENT -> tournamentLabel
    }
}

data class Dance(
    val id: String,
    val name: String,
    val category: DanceCategory,
    val meter: Int, // 2 for 2/4, 3 for 3/4, 4 for 4/4
    val altMeter: Int? = null,
    val tpmIdealMin: Int,
    val tpmIdealMax: Int,
    val bpmScaleMin: Int,
    val bpmScaleMax: Int,
    val idealBpmMin: Int,
    val idealBpmMax: Int,
    val canHalfTap: Boolean = false,
    val halfBpmMin: Int? = null,
    val halfBpmMax: Int? = null,
    val rhythmHint: String? = null
)

data class DanceMatch(
    val dance: Dance,
    val effectiveBpm: Int,
    val isHalfSpeed: Boolean,
    val percentage: Float, // 0.0 .. 1.0 on the scale
    val status: TempoStatus,
    val idealStartPercentage: Float,
    val idealEndPercentage: Float,
    val diffToCenter: Float
)

data class DanceMismatch(
    val dance: Dance,
    val reason: String,
    val isMeterMismatch: Boolean,
    val isTooSlow: Boolean,
    val isTooFast: Boolean,
    val bpmDifference: Int? = null
)

data class EvaluationResult(
    val matches: List<DanceMatch>,
    val mismatches: List<DanceMismatch>
)
