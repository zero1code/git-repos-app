package br.com.mpsystems.cpmtracking.gitrepos.domain.repository.repos

import br.com.mpsystems.cpmtracking.gitrepos.data.api.GitHubApi
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Owner
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Repo
import br.com.mpsystems.cpmtracking.gitrepos.data.database.dao.GitUserDao
import br.com.mpsystems.cpmtracking.gitrepos.domain.repository.repos.ReposRepository
import br.com.mpsystems.cpmtracking.gitrepos.util.Resource
import javax.inject.Inject

class ReposReposRepositoryImpl @Inject constructor(
    private val api: GitHubApi,
    private val gitUserDao: GitUserDao
) : ReposRepository {
    override suspend fun listRepository(user: String): Resource<List<Repo>> {
        return try {
            val response = api.getRepos(user)
            val result = response.body()

            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Alguma coisa deu errado.")
        }
    }

    override suspend fun listUsers(): Resource<List<Owner>> {
        return Resource.Success(mutableListOf())
    }

    override suspend fun insertUser(owner: Owner): Long {
        return gitUserDao.insert(owner)
    }
}