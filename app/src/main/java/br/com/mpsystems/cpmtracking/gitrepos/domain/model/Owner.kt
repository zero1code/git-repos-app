package br.com.mpsystems.cpmtracking.gitrepos.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tb_searched_users", indices = [Index(value = ["login"], unique = true)])
data class Owner (
    @PrimaryKey()
    val login: String,
    @SerializedName("avatar_url")
    val avatarURL: String
)