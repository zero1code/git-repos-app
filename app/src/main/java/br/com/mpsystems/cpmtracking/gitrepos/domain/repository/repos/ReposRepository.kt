package br.com.mpsystems.cpmtracking.gitrepos.domain.repository.repos

import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Owner
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Repo
import br.com.mpsystems.cpmtracking.gitrepos.util.Resource

interface ReposRepository {
    suspend fun listRepository(user: String) : Resource<List<Repo>>

    suspend fun insertFavorite(repo: Repo) : Long

    suspend fun insertUser(owner: Owner) : Long
}