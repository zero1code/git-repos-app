package br.com.mpsystems.cpmtracking.gitrepos.domain.repository.favorites

import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Repo
import br.com.mpsystems.cpmtracking.gitrepos.util.Resource

interface FavoritesRepository {
    suspend fun findAllFavorites() : Resource<List<Repo>>

    fun deleteFavorite(id: Long) : Int

}