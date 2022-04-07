package br.com.mpsystems.cpmtracking.gitrepos.presentation.fragment.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import br.com.mpsystems.cpmtracking.gitrepos.R
import br.com.mpsystems.cpmtracking.gitrepos.databinding.FragmentFavoritesBinding
import br.com.mpsystems.cpmtracking.gitrepos.presentation.adapter.FavoriteListAdapter
import br.com.mpsystems.cpmtracking.gitrepos.util.DividerItemDecoration
import br.com.mpsystems.cpmtracking.gitrepos.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var binding: FragmentFavoritesBinding? = null
    private val viewModel: FavoritesViewModel by viewModels()
    private val adapter by lazy { FavoriteListAdapter() }
    private var favoritesStateJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFavoritesBinding.bind(view)

        binding?.let {
            it.rvRepos.adapter = adapter
            it.rvRepos.addItemDecoration(DividerItemDecoration(requireContext()))
        }

        insertListeners()
    }

    override fun onResume() {
        viewModel.getFavorites()
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        setupLifecycle()
    }

    override fun onStop() {
        favoritesStateJob?.cancel()
        super.onStop()
    }

    @SuppressLint("ResourceType")
    private fun setupLifecycle() {
        favoritesStateJob = lifecycleScope.launchWhenStarted {
            viewModel.favoriteState.collect { event ->
                when (event) {
                    FavoritesViewModel.FavoriteApiResult.Empty -> {
                        Toast.makeText(requireContext(), "Vazio", Toast.LENGTH_SHORT).show()
                    }
                    is FavoritesViewModel.FavoriteApiResult.Failure -> {
                        binding?.root?.showSnackBar(event.errorText, resources.getString(R.color.red))?.show()
                    }
                    FavoritesViewModel.FavoriteApiResult.Loading -> {
                        Toast.makeText(requireContext(), "Carregando", Toast.LENGTH_SHORT).show()
                    }
                    is FavoritesViewModel.FavoriteApiResult.Success -> {
                        adapter.submitList(event.lista)
                    }
                    is FavoritesViewModel.FavoriteApiResult.Deleted -> {
                        adapter.submitList(event.lista)
                        binding?.root?.showSnackBar("Favorito excluído.", resources.getString(R.color.green))?.show()
                    }
                }
            }
        }
    }

    private fun insertListeners() {
        adapter.listenerDeleteFavorite = { position ->
            viewModel.deleteFavorite(adapter.currentList[position])
            adapter.notifyItemRemoved(position)
        }
    }
}