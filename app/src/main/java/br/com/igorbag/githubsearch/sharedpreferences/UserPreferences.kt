package br.com.igorbag.githubsearch.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {

    private val dataUser: SharedPreferences =
        context.getSharedPreferences("UsersGithub", Context.MODE_PRIVATE)

    fun storeUser(key: String, user: String) {
        dataUser.edit().putString(key, user).apply()
    }

    fun getUser(key: String) : String {
        return dataUser.getString(key, "") ?: ""
    }
}