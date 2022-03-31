package br.com.mpsystems.cpmtracking.gitrepos.data.di

import br.com.mpsystems.cpmtracking.gitrepos.data.api.GitHubApi
import br.com.mpsystems.cpmtracking.gitrepos.domain.repository.repos.ReposReposRepositoryImpl
import br.com.mpsystems.cpmtracking.gitrepos.domain.repository.DispatcherProvider
import br.com.mpsystems.cpmtracking.gitrepos.domain.repository.repos.ReposRepository
import br.com.mpsystems.cpmtracking.gitrepos.data.database.dao.GitUserDao
import br.com.mpsystems.cpmtracking.gitrepos.domain.repository.users.UsersReposRepositoryImpl
import br.com.mpsystems.cpmtracking.gitrepos.domain.repository.users.UsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private val BASE_URL = "https://api.github.com/"

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providerGitHubApi() : GitHubApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GitHubApi::class.java)

    @Singleton
    @Provides
    fun provideMainRepository(
        api: GitHubApi,
        gitUserDao: GitUserDao
    ) : ReposRepository = ReposReposRepositoryImpl(api, gitUserDao)

    @Singleton
    @Provides
    fun provideUsersRepository(
        gitUserDao: GitUserDao
    ) : UsersRepository = UsersReposRepositoryImpl(gitUserDao)

    @Singleton
    @Provides
    fun providerDispatchers() : DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined

    }
}