package com.jonwal.dancetempo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonwal.dancetempo.model.Song
import com.jonwal.dancetempo.model.SongSortOrder
import com.jonwal.dancetempo.repository.SongRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SongLibraryUiState(
    val songs: List<Song> = emptyList(),
    val totalCount: Int = 0,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val selectedDanceIds: Set<String> = emptySet(), // Empty means "Alle"
    val sortOrder: SongSortOrder = SongSortOrder.NEWEST_FIRST,
    val selectedSongForDetail: Song? = null
)

class SongLibraryViewModel(private val repository: SongRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _isSearchActive = MutableStateFlow(false)
    private val _selectedDanceIds = MutableStateFlow<Set<String>>(emptySet())
    private val _sortOrder = MutableStateFlow(SongSortOrder.NEWEST_FIRST)
    private val _selectedSongForDetail = MutableStateFlow<Song?>(null)

    val uiState: StateFlow<SongLibraryUiState> = combine(
        repository.songsStream,
        combine(_searchQuery, _isSearchActive, _selectedDanceIds) { q, active, dances ->
            Triple(q, active, dances)
        },
        _sortOrder,
        _selectedSongForDetail
    ) { allSongs, filterTriple, sortOrder, detailSong ->
        val (query, isSearchActive, selectedDances) = filterTriple
        // 1. Filter by Search Query
        val queryFiltered = if (query.isBlank()) {
            allSongs
        } else {
            val q = query.trim().lowercase()
            allSongs.filter {
                it.title.lowercase().contains(q) ||
                        it.artist.lowercase().contains(q) ||
                        (it.notes?.lowercase()?.contains(q) == true)
            }
        }

        // 2. Filter by Dance IDs (Multi-Select: song matches if it has at least one of the selected dances)
        val danceFiltered = if (selectedDances.isEmpty()) {
            queryFiltered
        } else {
            queryFiltered.filter { song ->
                song.dances.any { selectedDances.contains(it.id) }
            }
        }

        // 3. Sort
        val sorted = when (sortOrder) {
            SongSortOrder.RATING_DESC -> danceFiltered.sortedWith(
                compareByDescending<Song> { it.rating }
                    .thenByDescending { it.createdAt }
            )
            SongSortOrder.BPM_ASC -> danceFiltered.sortedWith(
                compareBy<Song> { it.bpm }
                    .thenByDescending { it.rating }
            )
            SongSortOrder.BPM_DESC -> danceFiltered.sortedWith(
                compareByDescending<Song> { it.bpm }
                    .thenByDescending { it.rating }
            )
            SongSortOrder.NEWEST_FIRST -> danceFiltered.sortedByDescending { it.createdAt }
        }

        SongLibraryUiState(
            songs = sorted,
            totalCount = allSongs.size,
            searchQuery = query,
            isSearchActive = isSearchActive,
            selectedDanceIds = selectedDances,
            sortOrder = sortOrder,
            selectedSongForDetail = detailSong
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SongLibraryUiState()
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun toggleSearchActive() {
        _isSearchActive.update { !it }
        if (!_isSearchActive.value) {
            _searchQuery.value = ""
        }
    }

    fun toggleDanceFilter(danceId: String) {
        _selectedDanceIds.update { current ->
            if (current.contains(danceId)) {
                current - danceId
            } else {
                current + danceId
            }
        }
    }

    fun clearDanceFilter() {
        _selectedDanceIds.value = emptySet()
    }

    fun setSortOrder(order: SongSortOrder) {
        _sortOrder.value = order
    }

    fun selectSongForDetail(song: Song?) {
        _selectedSongForDetail.value = song
    }

    fun deleteSong(songId: Long) {
        viewModelScope.launch {
            repository.deleteSong(songId)
            if (_selectedSongForDetail.value?.id == songId) {
                _selectedSongForDetail.value = null
            }
        }
    }
}
