package br.com.igorbag.githubsearch.listeners

interface RepositoryListeners {

    fun shareRepositoryLink(urlRepository: String)

    fun openBrowser(urlRepository: String)
}