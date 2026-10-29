package com.jonwal.dancetempo.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val artist: String,
    val bpm: Int,
    val meter: Int, // 2, 3, 4
    val notes: String? = null,
    @ColumnInfo(name = "streaming_url")
    val streamingUrl: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
