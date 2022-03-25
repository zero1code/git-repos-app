package br.com.mpsystems.cpmtracking.gitrepos.data.di

import br.com.mpsystems.cpmtracking.gitrepos.data.api.GitHubApi
import br.com.mpsystems.cpmtracking.gitrepos.data.repositories.DefaultMainRepository
import br.com.mpsystems.cpmtracking.gitrepos.data.repositories.DispatcherProvider
import br.com.mpsystems.cpmtracking.gitrepos.data.repositories.MainRepository
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
    fun provideMainRepository(api: GitHubApi) : MainRepository = DefaultMainRepository(api)

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