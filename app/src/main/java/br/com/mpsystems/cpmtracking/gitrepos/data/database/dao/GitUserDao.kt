package br.com.mpsystems.cpmtracking.gitrepos.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.mpsystems.cpmtracking.gitrepos.data.model.Owner
import kotlinx.coroutines.flow.Flow

@Dao
interface GitUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Owner): Long

    @Query(value = "SELECT * FROM tb_user")
    fun findAll(): Flow<List<Owner>>
}