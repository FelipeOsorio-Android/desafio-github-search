package br.com.igorbag.githubsearch.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.igorbag.githubsearch.data.GitHubService
import br.com.igorbag.githubsearch.databinding.ActivityMainBinding
import br.com.igorbag.githubsearch.domain.Repository
import br.com.igorbag.githubsearch.listeners.RepositoryListeners
import br.com.igorbag.githubsearch.sharedpreferences.UserPreferences
import br.com.igorbag.githubsearch.ui.adapter.UserReposAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), TextWatcher {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val btnSave by lazy { binding.buttonMainSave }
    private val editNameUser by lazy { binding.editMainUser }
    private val listRepository by lazy { binding.recyclerMainRepositories }


    private lateinit var githubApi: GitHubService
    private lateinit var adapter: UserReposAdapter
    private lateinit var listener: RepositoryListeners

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //Configuração Retrofit
        setupRetrofit()

        //Listener para a ViewHolder
        listener = object : RepositoryListeners {
            // Metodo responsavel por compartilhar o link do repositorio selecionado
            override fun shareRepositoryLink(urlRepository: String) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, urlRepository)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }

            // Metodo responsavel por abrir o browser com o link informado do repositorio
            override fun openBrowser(urlRepository: String) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(urlRepository)
                    )
                )
            }
        }

        //Configuração Adapter
        setupAdapter()

    }

    override fun onResume() {
        super.onResume()

        //Listener Edit
        editNameUser.addTextChangedListener(this)

        //Listener Button
        btnSave.setOnClickListener { saveUserLocal(editNameUser.text.toString()) }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val editUser = editNameUser.text.toString()
        btnSave.isEnabled = editUser.isNotEmpty() && editUser.isNotBlank()
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        githubApi = retrofit.create(GitHubService::class.java)
    }

    private fun saveUserLocal(user: String) {
        UserPreferences(this).storeUser("USER_NAME", user)

        showUserName("USER_NAME")
    }

    private fun showUserName(key: String) {
        val userGitHub = UserPreferences(this).getUser(key)

        getAllReposByUserName(userGitHub)
    }

    private fun getAllReposByUserName(user: String) {
        githubApi.getAllRepositoriesByUser(user).enqueue(object : Callback<List<Repository>> {
            override fun onResponse(call: Call<List<Repository>>, response: Response<List<Repository>>
            ) {
                when (response.isSuccessful) {
                    true -> response.body()?.let { adapter.getRepositoryList(it) }

                    false -> Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
                Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupAdapter() {
        adapter = UserReposAdapter()
        adapter.getListeners(listener)
        listRepository.adapter = adapter
    }
}