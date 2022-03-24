package br.com.mpsystems.cpmtracking.gitrepos.data.api

import br.com.mpsystems.cpmtracking.gitrepos.data.model.Repo
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {

    @GET("users/{user}/repos")
    suspend fun getRepos(@Path("user") user: String?): List<Repo>
}