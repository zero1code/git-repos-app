package br.com.mpsystems.cpmtracking.gitrepos.domain.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tb_favorites", indices = [Index(value = ["id"], unique = true)])
data class Repo (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    @Embedded val owner: Owner,
    @SerializedName("stargazers_count")
    val stargazersCount: Long,
    val language: String,
    @SerializedName("html_url")
    val htmlURL: String,
    val description: String?
)
