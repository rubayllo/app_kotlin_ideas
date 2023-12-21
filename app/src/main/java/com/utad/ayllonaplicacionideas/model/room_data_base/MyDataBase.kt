package com.utad.ayllonaplicacionideas.model.room_data_base

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.utad.ayllonaplicacionideas.model.room_data_base.dao_data_base.DaoDataBase
import com.utad.ayllonaplicacionideas.model.room_data_base.entities.Detail
import com.utad.ayllonaplicacionideas.model.room_data_base.entities.Idea
import com.utad.ayllonaplicacionideas.model.room_data_base.entities.ImageTypeConverters

@Database(entities = [Idea::class, Detail::class], version = 1)
@TypeConverters(ImageTypeConverters::class)
abstract class MyDataBase: RoomDatabase() {
    abstract fun daoDataBase(): DaoDataBase
}