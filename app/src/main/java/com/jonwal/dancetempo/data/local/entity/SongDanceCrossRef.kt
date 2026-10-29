package com.jonwal.dancetempo.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "song_dance_cross_ref",
    primaryKeys = ["song_id", "dance_id"],
    foreignKeys = [
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = ["id"],
            childColumns = ["song_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["song_id"]),
        Index(value = ["dance_id"])
    ]
)
data class SongDanceCrossRef(
    @ColumnInfo(name = "song_id")
    val songId: Long,
    @ColumnInfo(name = "dance_id")
    val danceId: String
)
