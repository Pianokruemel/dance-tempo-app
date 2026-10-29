package com.jonwal.dancetempo.viewmodel

import androidx.lifecycle.ViewModel
import com.jonwal.dancetempo.logic.TempoAnalyzer
import com.jonwal.dancetempo.model.DanceLevelMode
import com.jonwal.dancetempo.model.DanceMatch
import com.jonwal.dancetempo.model.DanceMismatch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DanceTempoUiState(
    val isBeat1ModeActive: Boolean = false,
    val danceLevelMode: DanceLevelMode = DanceLevelMode.HOBBY, // Default to Hobby
    val currentBpm: Int? = null,
    val detectedMeter: Int? = null, // e.g. 2, 3, 4
    val currentTpm: Int? = null,
    val matches: List<DanceMatch> = emptyList(),
    val mismatches: List<DanceMismatch> = emptyList(),
    val tapCount: Int = 0
)

class DanceTempoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DanceTempoUiState())
    val uiState: StateFlow<DanceTempoUiState> = _uiState.asStateFlow()

    private val timestamps = mutableListOf<Long>()
    private val downbeatIndices = mutableListOf<Int>()
    private val tapTypes = mutableListOf<Boolean>() // true if beat1

    fun toggleBeat1Mode() {
        _uiState.update { it.copy(isBeat1ModeActive = !it.isBeat1ModeActive) }
        reset()
    }

    fun setDanceLevelMode(mode: DanceLevelMode) {
        _uiState.update { it.copy(danceLevelMode = mode) }
        recomputeAnalysis()
    }

    fun onUserTap(isBeat1: Boolean) {
        val now = System.currentTimeMillis()

        // Auto-reset on pause
        if (timestamps.isNotEmpty() && (now - timestamps.last() > TempoAnalyzer.TIMEOUT_RESET_MS)) {
            resetInternal()
        }

        timestamps.add(now)
        tapTypes.add(isBeat1)

        if (isBeat1) {
            downbeatIndices.add(timestamps.size - 1)
        }

        // Keep rolling history of last 20 taps
        if (timestamps.size > 20) {
            timestamps.removeAt(0)
            tapTypes.removeAt(0)
            downbeatIndices.clear()
            tapTypes.forEachIndexed { idx, b1 -> if (b1) downbeatIndices.add(idx) }
        }

        recomputeAnalysis()
    }

    fun reset() {
        resetInternal()
        _uiState.update {
            it.copy(
                currentBpm = null,
                detectedMeter = null,
                currentTpm = null,
                matches = emptyList(),
                mismatches = emptyList(),
                tapCount = 0
            )
        }
    }

    private fun resetInternal() {
        timestamps.clear()
        tapTypes.clear()
        downbeatIndices.clear()
    }

    private fun recomputeAnalysis() {
        val bpm = TempoAnalyzer.calculateBpm(timestamps)
        val meter = if (_uiState.value.isBeat1ModeActive) {
            TempoAnalyzer.detectMeter(downbeatIndices)
        } else null

        val tpm = if (bpm != null) {
            val div = meter ?: 4
            (bpm.toFloat() / div).toInt()
        } else null

        val evaluation = if (bpm != null) {
            TempoAnalyzer.analyzeTempo(
                bpm = bpm,
                meter = meter,
                mode = _uiState.value.danceLevelMode
            )
        } else null

        _uiState.update {
            it.copy(
                currentBpm = bpm,
                detectedMeter = meter,
                currentTpm = tpm,
                matches = evaluation?.matches ?: emptyList(),
                mismatches = evaluation?.mismatches ?: emptyList(),
                tapCount = timestamps.size
            )
        }
    }
}
