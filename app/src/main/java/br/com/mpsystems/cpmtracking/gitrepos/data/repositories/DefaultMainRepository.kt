package br.com.mpsystems.cpmtracking.gitrepos.data.repositories

import br.com.mpsystems.cpmtracking.gitrepos.data.api.GitHubApi
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: GitHubApi
) : MainRepository {

}