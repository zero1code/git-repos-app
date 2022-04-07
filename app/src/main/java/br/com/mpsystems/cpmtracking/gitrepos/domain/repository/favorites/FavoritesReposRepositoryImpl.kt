package br.com.mpsystems.cpmtracking.gitrepos.domain.repository.favorites

import br.com.mpsystems.cpmtracking.gitrepos.data.database.dao.GitUserDao
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Repo
import br.com.mpsystems.cpmtracking.gitrepos.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesReposRepositoryImpl @Inject constructor(
    private val gitUserDao: GitUserDao
) : FavoritesRepository {
    override suspend fun findAllFavorites(): Resource<List<Repo>> {
        val list = gitUserDao.findAllFavorites()
        return if (list.isNotEmpty()) Resource.Success(list) else Resource.Error("Erro ao buscar os dados.")
    }
    override fun deleteFavorite(id: Long) : Int {
        return gitUserDao.deleteFavorite(id)
    }
}