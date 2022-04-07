package br.com.mpsystems.cpmtracking.gitrepos.data.database.dao

import androidx.room.*
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Owner
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Repo


@Dao
interface GitUserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(entity: Owner): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(entity: Repo): Long

    @Query(value = "SELECT * FROM tb_searched_users")
    fun findAllUsersSearched(): List<Owner>

    @Query(value = "SELECT * FROM tb_favorites")
    fun findAllFavorites(): List<Repo>

    @Query("DELETE from tb_favorites where id = :id")
    fun deleteFavorite(id: Long) : Int
}