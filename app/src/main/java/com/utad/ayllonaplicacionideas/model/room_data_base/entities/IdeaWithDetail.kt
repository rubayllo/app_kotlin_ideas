package com.utad.ayllonaplicacionideas.model.room_data_base.entities

import androidx.room.Embedded
import androidx.room.Relation

data class IdeaWithDetail(
    @Embedded
    val idea: Idea,
    @Relation(
          parentColumn = "id",
          entityColumn = "ideaID"
    )
    val details: List<Detail>
)