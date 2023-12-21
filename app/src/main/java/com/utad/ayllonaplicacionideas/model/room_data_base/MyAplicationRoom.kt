package com.utad.ayllonaplicacionideas.model.room_data_base

import android.app.Application
import androidx.room.Room

class MyAplicationRoom: Application() {

    //Instanciamos Room
    lateinit var room: MyDataBase

    override fun onCreate() {
        super.onCreate()
        room = Room.databaseBuilder(
            applicationContext,
            MyDataBase::class.java,
            "MyDataBase" ).build()
    }
}