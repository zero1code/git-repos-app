package br.com.mpsystems.cpmtracking.gitrepos.data.repositories

import br.com.mpsystems.cpmtracking.gitrepos.data.api.GitHubApi
import br.com.mpsystems.cpmtracking.gitrepos.data.model.Repo
import br.com.mpsystems.cpmtracking.gitrepos.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: GitHubApi
) : MainRepository {
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

}