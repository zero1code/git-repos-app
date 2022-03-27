package br.com.mpsystems.cpmtracking.gitrepos.data.repositories.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mpsystems.cpmtracking.gitrepos.data.model.Owner
import br.com.mpsystems.cpmtracking.gitrepos.data.model.Repo
import br.com.mpsystems.cpmtracking.gitrepos.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class RepoApiResult {
        class Success(val lista: List<Repo>) : RepoApiResult()
        class Failure(val errorText: String) : RepoApiResult()
        object Loading : RepoApiResult()
        object Empty : RepoApiResult()
    }

    private val _repoList = MutableStateFlow<RepoApiResult>(RepoApiResult.Empty)
    val repoList: StateFlow<RepoApiResult> = _repoList

    fun getRepoList(
        user: String
    ) {
        viewModelScope.launch(dispatchers.io) {
            _repoList.value = RepoApiResult.Loading
            when(val response = repository.listRepository(user)) {
                is Resource.Error -> _repoList.value = RepoApiResult.Failure(response.message!!)
                is Resource.Success -> {
                    val repos = response.data
                    _repoList.value = RepoApiResult.Success(repos!!.toList())
                }
            }
        }
    }

    fun getUsersSearched() {
        viewModelScope.launch(dispatchers.io) {

        }
    }

    fun insertUser(owner: Owner) {
        viewModelScope.launch(dispatchers.io) {
            repository.insertUser(owner)
        }
    }
}