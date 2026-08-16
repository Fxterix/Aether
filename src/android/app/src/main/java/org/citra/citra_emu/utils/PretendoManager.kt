package com.aether.emulator.utils

object PretendoManager {
    init {
        System.loadLibrary("aether-android")
    }

    // Activar los parches de estabilidad de red para Pretendo
    external fun nativeInitPretendoFixes(enable: Boolean)
}
