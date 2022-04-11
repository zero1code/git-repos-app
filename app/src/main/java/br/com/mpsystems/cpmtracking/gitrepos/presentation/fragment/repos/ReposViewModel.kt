package br.com.mpsystems.cpmtracking.gitrepos.presentation.fragment.repos

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Owner
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Repo
import br.com.mpsystems.cpmtracking.gitrepos.domain.repository.DispatcherProvider
import br.com.mpsystems.cpmtracking.gitrepos.domain.repository.favorites.FavoritesRepository
import br.com.mpsystems.cpmtracking.gitrepos.domain.repository.repos.ReposRepository
import br.com.mpsystems.cpmtracking.gitrepos.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ReposViewModel @ViewModelInject constructor(
    private val repository: ReposRepository,
    private val favoritesRepository: FavoritesRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class RepoApiResult {
        class Success(val lista: List<Repo>) : RepoApiResult()
        class Failure(val errorText: String) : RepoApiResult()
        object SuccessUser : RepoApiResult()
        object SuccessFavorite : RepoApiResult()
        object Loading : RepoApiResult()
        object Empty : RepoApiResult()
        object Nothing : RepoApiResult()
    }

    private val _repoList = MutableStateFlow<RepoApiResult>(RepoApiResult.Empty)
    val repoList: StateFlow<RepoApiResult> get() = _repoList

    fun getRepoList(
        user: String
    ) {
        viewModelScope.launch(dispatchers.io) {
            _repoList.value = RepoApiResult.Loading
            when (val response = repository.listRepository(user)) {
                is Resource.Error -> _repoList.value = RepoApiResult.Failure(response.message!!)
                is Resource.Success -> {
                    when (val favResponse = favoritesRepository.findAllFavorites()) {
                        is Resource.Error -> {
                            _repoList.value = RepoApiResult.Success(response.data!!.toList())
                        }
                        is Resource.Success -> {
                            val favorites = favResponse.data!!.toList()

                            favorites.forEach { favorite ->
                                response.data?.find { it.id == favorite.id }?.isFavorite = 1
                            }
                            if (response.data!!.toList().isNotEmpty()) {
                                _repoList.value = RepoApiResult.Success(response.data.toList())
                            } else {
                                _repoList.value =
                                    RepoApiResult.Failure("Nenhum repositório encontrado.")
                            }
                        }
                    }

                }
            }
        }
    }

    fun nothingToDo() {
        viewModelScope.launch(dispatchers.io) {
            _repoList.value = RepoApiResult.Nothing
        }
    }

    fun insertUser(owner: Owner) {
        viewModelScope.launch(dispatchers.io) {
            val newUser = repository.insertUser(owner)
            if (newUser > 0) {
                _repoList.value = RepoApiResult.SuccessUser
            }
        }
    }

    fun insertFavorite(repo: Repo) {
        viewModelScope.launch(dispatchers.io) {
            val newFavorite = repository.insertFavorite(repo)
            if (newFavorite > 0) {
                _repoList.value = RepoApiResult.SuccessFavorite
            }
        }
    }
}