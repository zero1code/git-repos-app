package br.com.mpsystems.cpmtracking.gitrepos.data.repositories

import br.com.mpsystems.cpmtracking.gitrepos.data.model.Repo
import br.com.mpsystems.cpmtracking.gitrepos.util.Resource

interface MainRepository {
    suspend fun listRepository(user: String) : Resource<List<Repo>>

}