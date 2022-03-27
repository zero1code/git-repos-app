package br.com.mpsystems.cpmtracking.gitrepos.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.mpsystems.cpmtracking.gitrepos.data.model.Owner
import br.com.mpsystems.cpmtracking.gitrepos.data.database.dao.GitUserDao

@Database(entities = [Owner::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gitUserDao(): GitUserDao
}