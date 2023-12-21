package com.utad.ayllonaplicacionideas.model.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "com.utad.ayllonaplicacionideas")

object DataStoreManager {
    val userNameKey: Preferences.Key<String> = stringPreferencesKey("user_name")
    val passwordKey: Preferences.Key<String> = stringPreferencesKey("user_password")
    val checkLogin: Preferences.Key<Boolean> = booleanPreferencesKey("user_login")

    suspend fun saveUser(context: Context, nameUser: String, password: String){
//     "suspend" para que la función se ejecute en segundo plano. En DataStore es necesario que las
//     funciones se ejecuten así. Al llevar la notación "suspend" hay que lanzarla dentro de una corrutina.
//     Una corrutina nos permite lanzar tareas de forma asíncrona en este caso lo ejecutamos en un hilo
//     secundario que son los que se utilizan para operaciones de lectura y escritura que en Android
//     se llama IO, ese hilo también se utiliza para hacer peticiones de red.

        context.dataStore.edit { editor->
            editor[userNameKey] = nameUser
            editor[passwordKey] = password
        }
    }

    suspend fun deleteUser(context: Context){
        context.dataStore.edit { editor->
            editor.clear()
        }
    }

    suspend fun setLoginCheckIn(context: Context){
        context.dataStore.edit { editor ->
            editor[checkLogin] = true
        }
    }


    suspend fun setLoginCheckOut(context: Context){
        context.dataStore.edit { editor ->
            editor[checkLogin] = false
        }
    }

    suspend fun getLoginCheck(context: Context): Flow<Boolean> {
        return  context.dataStore.data.map { editor ->
            editor[checkLogin] ?: false
        }
    }
    //    Flow en Kotlin es un flujo continuo de información a donde tu te puedes suscribir
//    y se realizan secuencias asincrónicas que se encargan de llevar a cabo la emisión
//    de valores de manera secuencial.
//    hay que implementar las dependencias en gradle:app
    suspend fun getUser(context: Context): Flow<String> {
        return context.dataStore.data.map { editor->
            editor[userNameKey] ?: "El valor es nulo"
        }
    }

    suspend fun getPassword(context: Context): Flow<String> {
        return context.dataStore.data.map { editor->
            editor[passwordKey] ?: "El valor es nulo"
        }
    }

//    He creado la clase de tipo data un poco más abajo para poder crear un objeto con los datos que
//    necesito recuperar en esat función
    suspend fun getAllDataUser(context: Context): Flow<UserProfile> {
        return context.dataStore.data.map { preferences ->
            UserProfile(
                name = preferences[stringPreferencesKey("user_name")].orEmpty(),
                password = preferences[stringPreferencesKey("user_password")].orEmpty()
            )
        }
    }

}

data class UserProfile(val name: String, val password: String){}