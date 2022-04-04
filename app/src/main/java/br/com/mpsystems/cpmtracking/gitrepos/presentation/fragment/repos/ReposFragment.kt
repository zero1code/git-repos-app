package br.com.mpsystems.cpmtracking.gitrepos.presentation.fragment.repos

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import br.com.mpsystems.cpmtracking.gitrepos.R
import br.com.mpsystems.cpmtracking.gitrepos.databinding.FragmentReposBinding
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Repo
import br.com.mpsystems.cpmtracking.gitrepos.presentation.adapter.RepoListAdapter
import br.com.mpsystems.cpmtracking.gitrepos.presentation.fragment.favorites.FavoritesViewModel
import br.com.mpsystems.cpmtracking.gitrepos.util.*
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReposFragment : Fragment(R.layout.fragment_repos), SearchView.OnQueryTextListener {

    private var binding: FragmentReposBinding? = null
    private val viewModel: ReposViewModel by viewModels()
    private val viewModelFavorite: FavoritesViewModel by viewModels()
    private lateinit var repos: List<Repo>
    private val adapter by lazy { RepoListAdapter() }
    private val dialog by lazy { activity?.createProgressDialog() }
    private var user: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.actionBar
        setHasOptionsMenu(true)

        binding = FragmentReposBinding.bind(view)

        binding?.let {
            it.rvRepos.adapter = adapter
            it.rvRepos.hasFixedSize()
            it.rvRepos.addItemDecoration(DividerItemDecoration(requireContext()))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.repoList.collect { event ->
                when (event) {
                    ReposViewModel.RepoApiResult.Empty -> {
                        activity?.createDialog {
                            setMessage("Faça a pesquisa de um usuário para ver os repositórios.")
                        }?.show()
                    }
                    is ReposViewModel.RepoApiResult.Failure -> {
                        activity?.createDialog {
                            setMessage("Erro.")
                        }?.show()
                    }
                    ReposViewModel.RepoApiResult.Loading -> {
                        dialog?.show()
                    }
                    is ReposViewModel.RepoApiResult.Success -> {
                        repos = event.lista
                        adapter.submitList(repos)
                        viewModel.insertUser(event.lista[0].owner)

                        binding?.let {
                            it.tvUser.text = event.lista[0].owner.login
                            Glide.with(requireContext()).load(event.lista[0].owner.avatarURL).into(it.ivUser)
                        }
                        dialog?.dismiss()
                    }
                }
            }
        }

        insertListeners()
    }

    override fun onResume() {
        super.onResume()
        if (user.isNotEmpty()) viewModel.getRepoList(user)
    }

    @SuppressLint("ResourceType")
    private fun insertListeners() {
        adapter.listenerFavorite = { position ->
            val repo = adapter.currentList[position]

            if (repo.isFavorite == 1) {
                viewModelFavorite.deleteFavorite(repo.id)
                repos.find { it.id == repo.id }?.isFavorite = 0
                binding?.root?.showSnackBar("Favorito removido.", resources.getString(R.color.red))?.show()
            } else {
                viewModel.insertFavorite(repo)
                repos.find { it.id == repo.id }?.isFavorite = 1
                binding?.root?.showSnackBar("Favorito adicionado.", resources.getString(R.color.green))?.show()
            }

            adapter.notifyItemChanged(position)
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
        binding?.root?.hideSoftKeyboard()
        query?.let {
            viewModel.getRepoList(it)
            user = it
        }
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