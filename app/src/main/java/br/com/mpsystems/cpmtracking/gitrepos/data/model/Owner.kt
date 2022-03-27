package br.com.mpsystems.cpmtracking.gitrepos.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tb_user")
data class Owner (
    @PrimaryKey(autoGenerate = true)
    val idUser: Long = 0L,
    val login: String,
    @SerializedName("avatar_url")
    val avatarURL: String
)