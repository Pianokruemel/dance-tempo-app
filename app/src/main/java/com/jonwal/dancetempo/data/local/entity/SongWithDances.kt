package com.jonwal.dancetempo.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SongWithDances(
    @Embedded
    val song: SongEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "song_id"
    )
    val danceRefs: List<SongDanceCrossRef>
)
