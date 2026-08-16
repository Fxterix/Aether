package com.aether.emulator.utils

import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent

object ControllerAutoMapper {

    data class ControllerProfile(
        val name: String,
        val buttonMap: Map<Int, Int> // Mapeo de Android Keycode a botones de 3DS
    )

    // Perfiles predeterminados basados en el nombre del dispositivo
    private val genericGamepadProfile = ControllerProfile(
        name = "Generic Gamepad",
        buttonMap = mapOf(
            KeyEvent.KEYCODE_BUTTON_A to 0, // A
            KeyEvent.KEYCODE_BUTTON_B to 1, // B
            KeyEvent.KEYCODE_BUTTON_X to 2, // X
            KeyEvent.KEYCODE_BUTTON_Y to 3, // Y
            KeyEvent.KEYCODE_BUTTON_L1 to 4, // L
            KeyEvent.KEYCODE_BUTTON_R1 to 5, // R
            KeyEvent.KEYCODE_BUTTON_SELECT to 6, // Select
            KeyEvent.KEYCODE_BUTTON_START to 7  // Start
        )
    )

    fun detectAndMapController(device: InputDevice): ControllerProfile {
        val deviceName = device.name ?: "Unknown Controller"
        
        // Aquí puedes añadir reglas específicas si un mando cambia los códigos (ej. Xbox vs PS)
        return when {
            deviceName.contains("Xbox", ignoreCase = true) -> {
                ControllerProfile(deviceName, genericGamepadProfile.buttonMap)
            }
            deviceName.contains("Wireless Controller", ignoreCase = true) -> {
                // Perfil para mandos de PlayStation conectados por Bluetooth
                ControllerProfile(deviceName, genericGamepadProfile.buttonMap)
            }
            else -> {
                ControllerProfile(deviceName, genericGamepadProfile.buttonMap)
            }
        }
    }
}
