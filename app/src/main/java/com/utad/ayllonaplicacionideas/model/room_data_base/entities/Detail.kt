package com.utad.ayllonaplicacionideas.model.room_data_base.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//Detail. Será una tabla/book para almacenar los detalles/pasos/requisitos de
//una idea. La relación con la tabla Idea será “one-to-many”. Esta tabla tendrá: id,
//descripción del detalle y el identificador de la Idea a la que pertenece.
@Entity
data class Detail(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val descripcion: String,
    val ideaID: Int
)
