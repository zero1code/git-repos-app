package br.com.mpsystems.cpmtracking.gitrepos.domain.repository.users

import br.com.mpsystems.cpmtracking.gitrepos.data.database.dao.GitUserDao
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Owner
import br.com.mpsystems.cpmtracking.gitrepos.util.Resource
import javax.inject.Inject

class UsersReposRepositoryImpl @Inject constructor(
    private val gitUserDao: GitUserDao
) : UsersRepository {

    override suspend fun listUsers(): Resource<List<Owner>> {
        val list = gitUserDao.findAll()
        return if (list.isNotEmpty()) Resource.Success(list) else Resource.Error("Erro ao buscar os dados.")
    }
}