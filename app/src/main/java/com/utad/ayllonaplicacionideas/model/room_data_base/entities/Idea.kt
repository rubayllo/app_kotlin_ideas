package com.utad.ayllonaplicacionideas.model.room_data_base.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//Idea. la cual como mínimo deberá tener: un id, nombre y descripción.
//Óptimamente, tendrá también una imagen (Bitmap) y algún atributo más que
//quieras añadir.

@Entity
data class Idea(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val prioridad: String,
    val estado: String?,

//    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val image: Bitmap?
)


