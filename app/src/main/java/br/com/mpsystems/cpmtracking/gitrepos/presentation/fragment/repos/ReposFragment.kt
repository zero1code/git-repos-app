package br.com.mpsystems.cpmtracking.gitrepos.presentation.fragment.repos

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
import br.com.mpsystems.cpmtracking.gitrepos.databinding.FragmentReposBinding
import br.com.mpsystems.cpmtracking.gitrepos.presentation.adapter.RepoListAdapter
import br.com.mpsystems.cpmtracking.gitrepos.util.DividerItemDecoration
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReposFragment : Fragment(R.layout.fragment_repos), SearchView.OnQueryTextListener {

    private var binding: FragmentReposBinding? = null
    private val viewModel: ReposViewModel by viewModels()
    private val adapter by lazy { RepoListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.actionBar
        setHasOptionsMenu(true)

        binding = FragmentReposBinding.bind(view)

        binding?.let {
            it.rvRepos.adapter = adapter
            it.rvRepos.addItemDecoration(DividerItemDecoration(requireContext()))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.repoList.collect { event ->
                when (event) {
                    ReposViewModel.RepoApiResult.Empty -> {
                        Toast.makeText(requireContext(), "Vazio", Toast.LENGTH_SHORT).show()
                    }
                    is ReposViewModel.RepoApiResult.Failure -> {
                        Toast.makeText(requireContext(), "Falhou", Toast.LENGTH_SHORT).show()
                    }
                    ReposViewModel.RepoApiResult.Loading -> {
                        Toast.makeText(requireContext(), "Carregando", Toast.LENGTH_SHORT).show()
                    }
                    is ReposViewModel.RepoApiResult.Success -> {
                        adapter.submitList(event.lista)
                        viewModel.insertUser(event.lista[0].owner)

                        binding?.let {
                            it.tvUser.text = event.lista[0].owner.login
                            Glide.with(requireContext()).load(event.lista[0].owner.avatarURL).into(it.ivUser)
                        }
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