package com.jonwal.dancetempo.repository

import com.jonwal.dancetempo.data.local.SongDao
import com.jonwal.dancetempo.data.local.entity.SongEntity
import com.jonwal.dancetempo.model.DanceRepository
import com.jonwal.dancetempo.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SongRepository(private val songDao: SongDao) {

    val songsStream: Flow<List<Song>> = songDao.getAllSongsWithDances().map { entities ->
        entities.map { item ->
            val mappedDances = item.danceRefs.mapNotNull { ref ->
                DanceRepository.allDances.find { it.id == ref.danceId }
            }
            Song(
                id = item.song.id,
                title = item.song.title,
                artist = item.song.artist,
                bpm = item.song.bpm,
                meter = item.song.meter,
                notes = item.song.notes,
                streamingUrl = item.song.streamingUrl,
                dances = mappedDances,
                createdAt = item.song.createdAt
            )
        }
    }

    suspend fun saveSong(
        title: String,
        artist: String,
        bpm: Int,
        meter: Int,
        notes: String?,
        streamingUrl: String?,
        danceIds: List<String>
    ): Long {
        val entity = SongEntity(
            title = title.trim(),
            artist = artist.trim(),
            bpm = bpm,
            meter = meter,
            notes = notes?.trim()?.ifBlank { null },
            streamingUrl = streamingUrl?.trim()?.ifBlank { null }
        )
        return songDao.insertSongWithDances(entity, danceIds)
    }

    suspend fun deleteSong(songId: Long) {
        songDao.deleteSongById(songId)
    }
}
