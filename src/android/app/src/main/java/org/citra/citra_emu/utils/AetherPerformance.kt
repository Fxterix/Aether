package com.aether.emulator.utils

object AetherPerformance {
    init {
        // Asegúrate de que coincida con el nombre de tu librería nativa compilada
        System.loadLibrary("aether-android")
    }

    // Método para activar las optimizaciones de gama baja desde un botón o menú
    external fun nativeSetLowEndOptimizations(enabled: Boolean)

    // Método para limpiar memoria RAM de forma manual o automática
    external fun nativeTrimMemory()
}
