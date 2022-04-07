package br.com.mpsystems.cpmtracking.gitrepos.presentation.fragment.favorites

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Repo
import br.com.mpsystems.cpmtracking.gitrepos.domain.repository.DispatcherProvider
import br.com.mpsystems.cpmtracking.gitrepos.domain.repository.favorites.FavoritesRepository
import br.com.mpsystems.cpmtracking.gitrepos.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class FavoritesViewModel @ViewModelInject constructor(
    private val repository: FavoritesRepository,
    private val dispatchers: DispatcherProvider,
) : ViewModel() {

    sealed class FavoriteApiResult {
        object Empty : FavoriteApiResult()
        object Loading : FavoriteApiResult()
        class Success(val lista: MutableList<Repo>) : FavoriteApiResult()
        class Failure(val errorText: String) : FavoriteApiResult()
        class Deleted(val lista: MutableList<Repo>) : FavoriteApiResult()
    }

    private val _favoriteState = MutableStateFlow<FavoriteApiResult>(FavoriteApiResult.Empty)
    val favoriteState: StateFlow<FavoriteApiResult> get() = _favoriteState
    private var favoriteList: MutableList<Repo>? = null

    fun getFavorites() {
        viewModelScope.launch(dispatchers.io) {
            _favoriteState.value = FavoriteApiResult.Loading
            when (val response = repository.findAllFavorites()) {
                is Resource.Error -> _favoriteState.value = FavoriteApiResult.Empty
                is Resource.Success -> {
                    favoriteList = response.data?.toMutableList()
                    _favoriteState.value = FavoriteApiResult.Success(favoriteList!!)
                }
            }
        }
    }

    fun deleteFavorite(favorite: Repo) {
        viewModelScope.launch(dispatchers.io) {

            _favoriteState.value = FavoriteApiResult.Loading
            if (repository.deleteFavorite(favorite.id) < 0) {
                _favoriteState.value = FavoriteApiResult.Failure("Erro ao excluir favorito.")
            } else {
                favoriteList?.remove(favorite)

                _favoriteState.value =
                    if (favoriteList?.isNotEmpty() == true) FavoriteApiResult.Deleted(favoriteList!!)
                    else FavoriteApiResult.Empty
            }
        }
    }
}