package br.com.mpsystems.cpmtracking.gitrepos.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Owner
import br.com.mpsystems.cpmtracking.gitrepos.data.database.dao.GitUserDao
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Repo

@Database(entities = [Owner::class, Repo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gitUserDao(): GitUserDao
}