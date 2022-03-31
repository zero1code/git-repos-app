package br.com.mpsystems.cpmtracking.gitrepos.domain.repository.users

import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Owner
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Repo
import br.com.mpsystems.cpmtracking.gitrepos.util.Resource

interface UsersRepository {

    suspend fun listUsers() : Resource<List<Owner>>
}