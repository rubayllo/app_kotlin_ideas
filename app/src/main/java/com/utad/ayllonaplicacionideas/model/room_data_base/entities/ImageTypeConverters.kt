package com.utad.ayllonaplicacionideas.model.room_data_base.entities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class ImageTypeConverters {

//    @TypeConverter
//    fun fromBitmap(bitmap: Bitmap): ByteArray{
//        val outPutStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outPutStream)
//        return outPutStream.toByteArray()
//    }
//
//    @TypeConverter
//    fun getImageBitMap(byteArray: ByteArray): Bitmap {
//        return BitmapFactory.decodeByteArray(byteArray,0, byteArray.size)
//    }
//}

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray? {
        //Si no recibes nada, devuelve un nulo
        if (bitmap == null) {
            return null
        }
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }


    //Debes añadir que sea nulable el parametro y lo que devuelve la función
    @TypeConverter
    fun getImageBitMap(byteArray: ByteArray?): Bitmap? {
        //Si no recibes nada, devuelve un nulo
        if (byteArray == null) {
            return null
        }
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}