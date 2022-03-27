package br.com.mpsystems.cpmtracking.gitrepos.data.repositories.main

import br.com.mpsystems.cpmtracking.gitrepos.data.model.Owner
import br.com.mpsystems.cpmtracking.gitrepos.data.model.Repo
import br.com.mpsystems.cpmtracking.gitrepos.util.Resource

interface MainRepository {
    suspend fun listRepository(user: String) : Resource<List<Repo>>

    suspend fun listUsers() : Resource<List<Owner>>

    suspend fun insertUser(owner: Owner) : Long

}