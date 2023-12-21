package com.utad.ayllonaplicacionideas.model.room_data_base.dao_data_base

import android.webkit.WebSettings.RenderPriority
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.utad.ayllonaplicacionideas.model.room_data_base.entities.Detail
import com.utad.ayllonaplicacionideas.model.room_data_base.entities.Idea
import com.utad.ayllonaplicacionideas.model.room_data_base.entities.IdeaWithDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoDataBase {

    @Transaction
    @Query("SELECT * FROM Idea WHERE id =:ideaId")
     fun getIdeaWithDetailById(ideaId: Int): Flow<IdeaWithDetail>

    // DAO IDEA
    //Para guardar datos nuevos en la lista
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveIdeaList(idea: Idea)

    @Query("SELECT * FROM Idea")
    fun getAllIdeaList(): List<Idea>

    @Delete
    fun deleteIdea(idea: Idea)

    @Delete
    fun deleteDetail(detail: Detail)

    @Query("DELETE FROM Detail WHERE ideaID=:ideaId")
    fun deleteIdeaWithDetail(ideaId: Int)

    //Devuelve la idea la cual coincida su ID
    @Query("SELECT * FROM Idea WHERE id=:ideaId")
    fun getIdeaById(ideaId: Int): Idea

    // Actualizar prioridad y estado de una idea
    @Query("UPDATE Idea SET prioridad=:priority, estado=:status WHERE id=:ideaId")
    fun updateIdeaPriorityAndStatus(priority: String, status: String, ideaId: Int)

    // DAO DETAIL
    //Para guardar datos nuevos en la lista
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDetailList(detail: Detail)

    @Query("SELECT * FROM Detail WHERE ideaID=:ideaId")
    fun getAllDetailList(ideaId: Int): List<Detail>
}