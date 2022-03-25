package br.com.mpsystems.cpmtracking.gitrepos.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import br.com.mpsystems.cpmtracking.gitrepos.R
import br.com.mpsystems.cpmtracking.gitrepos.data.repositories.MainViewModel
import br.com.mpsystems.cpmtracking.gitrepos.databinding.ActivityMainBinding
import br.com.mpsystems.cpmtracking.gitrepos.view.adapter.RepoListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()
    private val adapter by lazy { RepoListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.rvRepos.adapter = adapter

        lifecycleScope.launchWhenStarted {
            viewModel.repoList.collect { event ->
                when(event) {
                    MainViewModel.RepoApiResult.Empty -> {
                        Toast.makeText(applicationContext, "Vazio", Toast.LENGTH_SHORT).show()
                    }
                    is MainViewModel.RepoApiResult.Failure -> {
                        Toast.makeText(applicationContext, "Falhou", Toast.LENGTH_SHORT).show()
                    }
                    MainViewModel.RepoApiResult.Loading -> {
                        Toast.makeText(applicationContext, "Carregando", Toast.LENGTH_SHORT).show()
                    }
                    is MainViewModel.RepoApiResult.Success -> {
                        adapter.submitList(event.lista)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
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