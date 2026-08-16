package com.aether.emulator.utils

object EShopBridge {
    init {
        System.loadLibrary("aether-android")
    }

    external fun nativeApplyInfiniteMoney()
}
