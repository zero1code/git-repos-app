package br.com.mpsystems.cpmtracking.gitrepos.data.database.di

import android.content.Context
import androidx.room.Room
import br.com.mpsystems.cpmtracking.gitrepos.data.database.AppDatabase
import br.com.mpsystems.cpmtracking.gitrepos.data.database.dao.GitUserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideGitUserDao(appDatabase: AppDatabase) : GitUserDao {
        return appDatabase.gitUserDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "db.git_repo"
        ).build()
    }

}