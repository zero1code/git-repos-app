package br.com.mpsystems.cpmtracking.gitrepos.presentation.fragment.favorites

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
import br.com.mpsystems.cpmtracking.gitrepos.presentation.adapter.RepoListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites), SearchView.OnQueryTextListener {

    private var binding: FragmentFavoritesBinding? = null
    private val viewModel: FavoritesViewModel by viewModels()
    private val adapter by lazy { RepoListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.actionBar
        setHasOptionsMenu(true)

        binding = FragmentFavoritesBinding.bind(view)

        binding?.let {
            it.rvRepos.adapter = adapter
        }

        lifecycleScope.launchWhenStarted {
            viewModel.repoList.collect { event ->
                when (event) {
                    FavoritesViewModel.RepoApiResult.Empty -> {
                        Toast.makeText(requireContext(), "Vazio", Toast.LENGTH_SHORT).show()
                    }
                    is FavoritesViewModel.RepoApiResult.Failure -> {
                        Toast.makeText(requireContext(), "Falhou", Toast.LENGTH_SHORT).show()
                    }
                    FavoritesViewModel.RepoApiResult.Loading -> {
                        Toast.makeText(requireContext(), "Carregando", Toast.LENGTH_SHORT).show()
                    }
                    is FavoritesViewModel.RepoApiResult.Success -> {
                        adapter.submitList(event.lista)
                        viewModel.insertUser(event.lista[0].owner)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = "Pesquisar..."

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { viewModel.getRepoList(it) }
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