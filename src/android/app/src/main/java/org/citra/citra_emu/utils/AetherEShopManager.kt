package com.aether.emulator.utils

import android.content.Context
import android.content.SharedPreferences

object AetherEShopManager {
    private const val PREF_NAME = "AetherEShopPrefs"
    private const val KEY_PLAY_COINS = "play_coins"
    private const val KEY_ESHOP_BALANCE = "eshop_balance"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Dar dinero infinito / monedas al usuario
    fun giveInfiniteMoney(context: Context) {
        val prefs = getPrefs(context)
        prefs.edit()
            .putInt(KEY_PLAY_COINS, 999999) // Monedas de juego al máximo
            .putInt(KEY_ESHOP_BALANCE, 999999) // Saldo eShop ficticio al máximo
            .apply()
    }

    // Consultar el saldo actual para mostrarlo en la interfaz de la eShop simulada
    fun getBalance(context: Context): Int {
        val prefs = getPrefs(context)
        // Si nunca se ha inicializado, le inyectamos dinero infinito de una vez
        if (!prefs.contains(KEY_ESHOP_BALANCE)) {
            giveInfiniteMoney(context)
        }
        return prefs.getInt(KEY_ESHOP_BALANCE, 999999)
    }
}
