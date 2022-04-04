package br.com.mpsystems.cpmtracking.gitrepos.presentation.fragment.favorites

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Repo
import br.com.mpsystems.cpmtracking.gitrepos.domain.repository.DispatcherProvider
import br.com.mpsystems.cpmtracking.gitrepos.domain.repository.favorites.FavoritesRepository
import br.com.mpsystems.cpmtracking.gitrepos.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class FavoritesViewModel @ViewModelInject constructor(
    private val repository: FavoritesRepository,
    private val dispatchers: DispatcherProvider,
) : ViewModel() {

    sealed class FavoriteApiResult {
        class Success(val lista: List<Repo>) : FavoriteApiResult()
        class Failure(val errorText: String) : FavoriteApiResult()
        object Loading : FavoriteApiResult()
        object Empty : FavoriteApiResult()
        class Deleted(val lista: List<Repo>) : FavoriteApiResult()
    }

    private val _favoriteList = MutableStateFlow<FavoriteApiResult>(FavoriteApiResult.Empty)
    val favoriteList: Flow<FavoriteApiResult> = _favoriteList

    fun getFavorites() {
        viewModelScope.launch(dispatchers.io) {
            _favoriteList.value = FavoriteApiResult.Loading
            when(val response = repository.findAllFavorites()) {
                is Resource.Error -> _favoriteList.value = FavoriteApiResult.Empty
                is Resource.Success -> {
                    val favorites = response.data
                    _favoriteList.value = FavoriteApiResult.Success(favorites!!.toList())
                }
            }
        }
    }

    fun deleteFavorite(id: Long) {
        viewModelScope.launch(dispatchers.io) {
            repository.deleteFavorite(id)
            _favoriteList.value = FavoriteApiResult.Loading
            when(val response = repository.findAllFavorites()) {
                is Resource.Error -> _favoriteList.value = FavoriteApiResult.Empty
                is Resource.Success -> {
                    val favorites = response.data
                    _favoriteList.value = FavoriteApiResult.Deleted(favorites!!.toList())
                }
            }
        }
    }
}