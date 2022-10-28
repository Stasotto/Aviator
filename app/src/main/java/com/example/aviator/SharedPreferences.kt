package com.example.aviator

import android.content.Context

class SharedPreferences(private val context: Context) {
    companion object {
        const val SHARED_PREF_NAME = "AviatorPref"
        const val BALANCE = "Balance"
        const val DEFAULT_BALANCE = 5000
    }

    private val sharedPreferences by lazy {
        context.getSharedPreferences(
            SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        )
    }

    fun saveBalance(balance: Int) {
        sharedPreferences.edit().putInt(BALANCE, balance).apply()
    }

    fun getBalance(): Int {
        return sharedPreferences.getInt(BALANCE, DEFAULT_BALANCE)
    }
}