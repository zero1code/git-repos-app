package br.com.mpsystems.cpmtracking.gitrepos.presentation.fragment.users

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Owner
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Repo
import br.com.mpsystems.cpmtracking.gitrepos.domain.repository.DispatcherProvider
import br.com.mpsystems.cpmtracking.gitrepos.domain.repository.users.UsersRepository
import br.com.mpsystems.cpmtracking.gitrepos.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class UsersViewModel @ViewModelInject constructor(
    private val repository: UsersRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class UsersListResult {
        class Success(val lista: List<Owner>) : UsersListResult()
        class Failure(val errorText: String) : UsersListResult()
        object Loading : UsersListResult()
        object Empty : UsersListResult()
    }

    private val _userList = MutableStateFlow<UsersListResult>(UsersListResult.Empty)
    val userList: StateFlow<UsersListResult> get() = _userList

    fun getUsersSearched() {
        viewModelScope.launch(dispatchers.io) {
            _userList.value = UsersListResult.Loading
            when(val response = repository.listUsers()) {
                is Resource.Error -> _userList.value = UsersListResult.Failure(response.message!!)
                is Resource.Success -> {
                    val repos = response.data
                    _userList.value = UsersListResult.Success(repos!!.toList())
                }
            }
        }
    }
}