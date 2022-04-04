package br.com.mpsystems.cpmtracking.gitrepos.presentation.fragment.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import br.com.mpsystems.cpmtracking.gitrepos.R
import br.com.mpsystems.cpmtracking.gitrepos.databinding.FragmentFavoritesBinding
import br.com.mpsystems.cpmtracking.gitrepos.presentation.adapter.FavoriteListAdapter
import br.com.mpsystems.cpmtracking.gitrepos.presentation.fragment.repos.ReposFragment
import br.com.mpsystems.cpmtracking.gitrepos.util.DividerItemDecoration
import br.com.mpsystems.cpmtracking.gitrepos.util.hideSoftKeyboard
import br.com.mpsystems.cpmtracking.gitrepos.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites), SearchView.OnQueryTextListener {

    private var binding: FragmentFavoritesBinding? = null
    private val viewModel: FavoritesViewModel by viewModels()
    private val adapter by lazy { FavoriteListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFavoritesBinding.bind(view)

        binding?.let {
            it.rvRepos.adapter = adapter
            it.rvRepos.addItemDecoration(DividerItemDecoration(requireContext()))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.favoriteList.collect { event ->
                when (event) {
                    FavoritesViewModel.FavoriteApiResult.Empty -> {
                        Toast.makeText(requireContext(), "Vazio", Toast.LENGTH_SHORT).show()
                        adapter.submitList(null)
                    }
                    is FavoritesViewModel.FavoriteApiResult.Failure -> {
                        Toast.makeText(requireContext(), "Falhou", Toast.LENGTH_SHORT).show()
                    }
                    FavoritesViewModel.FavoriteApiResult.Loading -> {
                        Toast.makeText(requireContext(), "Carregando", Toast.LENGTH_SHORT).show()
                    }
                    is FavoritesViewModel.FavoriteApiResult.Success -> {
                        adapter.submitList(event.lista)
                    }
                    is FavoritesViewModel.FavoriteApiResult.Deleted -> {
                        adapter.submitList(event.lista)
                    }
                }
            }
        }
        insertListeners()
    }

    @SuppressLint("ResourceType")
    private fun insertListeners() {
        adapter.listenerDeleteFavorite = {
            viewModel.deleteFavorite(it.id)
            binding?.root?.showSnackBar("Favorito removido.", resources.getString(R.color.red))?.show()
            viewModel.getFavorites()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavorites()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = "Pesquisar..."

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
//        query?.let { viewModel.getRepoList(it) }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.e(TAG, "onQueryTextChange: $newText")
        return true
    }

    companion object {
        private const val TAG = "TAG"
    }
}