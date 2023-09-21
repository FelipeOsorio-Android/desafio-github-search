package br.com.igorbag.githubsearch.viewholder

import androidx.recyclerview.widget.RecyclerView
import br.com.igorbag.githubsearch.databinding.RepositoryItemBinding
import br.com.igorbag.githubsearch.domain.Repository
import br.com.igorbag.githubsearch.listeners.RepositoryListeners

class RepositoryViewHolder(
    private val item: RepositoryItemBinding,
    private val listener: RepositoryListeners
) : RecyclerView.ViewHolder(item.root) {

    fun bind(repository: Repository) {
        item.tvPreco.text = repository.name

        item.ivFavorite.setOnClickListener {
            listener.shareRepositoryLink(repository.htmlUrl)
        }

        item.clCardContent.setOnClickListener{
            listener.openBrowser(repository.htmlUrl)
        }
    }
}