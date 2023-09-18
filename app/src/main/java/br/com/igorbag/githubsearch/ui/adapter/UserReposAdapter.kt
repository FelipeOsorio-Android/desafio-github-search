package br.com.igorbag.githubsearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.igorbag.githubsearch.databinding.RepositoryItemBinding
import br.com.igorbag.githubsearch.domain.Repository
import br.com.igorbag.githubsearch.listeners.RepositoryListeners
import br.com.igorbag.githubsearch.viewholder.RepositoryViewHolder

class UserReposAdapter : RecyclerView.Adapter<RepositoryViewHolder>() {

    private var repositoryList: List<Repository> = arrayListOf()
    private lateinit var itemListener: RepositoryListeners

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val item = RepositoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoryViewHolder(item, itemListener)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(repositoryList[position])
    }

    override fun getItemCount(): Int = repositoryList.count()

    fun getRepositoryList(list: List<Repository>) {
        repositoryList = list
        notifyDataSetChanged()
    }

    fun getListeners(listener: RepositoryListeners) {
        itemListener = listener
    }

}

