package com.jonwal.dancetempo.data.local

import androidx.room.*
import com.jonwal.dancetempo.data.local.entity.SongDanceCrossRef
import com.jonwal.dancetempo.data.local.entity.SongEntity
import com.jonwal.dancetempo.data.local.entity.SongWithDances
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Transaction
    @Query("SELECT * FROM songs ORDER BY created_at DESC")
    fun getAllSongsWithDances(): Flow<List<SongWithDances>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: SongEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongDanceCrossRefs(crossRefs: List<SongDanceCrossRef>)

    @Transaction
    suspend fun insertSongWithDances(song: SongEntity, danceIds: List<String>): Long {
        val songId = insertSong(song)
        val refs = danceIds.map { SongDanceCrossRef(songId = songId, danceId = it) }
        insertSongDanceCrossRefs(refs)
        return songId
    }

    @Delete
    suspend fun deleteSong(song: SongEntity)

    @Query("DELETE FROM songs WHERE id = :songId")
    suspend fun deleteSongById(songId: Long)
}
