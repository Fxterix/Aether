import android.view.InputDevice
import com.aether.emulator.utils.ControllerAutoMapper

// Dentro de tu clase MainActivity o GameActivity:
override fun onInputDeviceAdded(deviceId: Int) {
    checkConnectedControllers()
}

override fun onInputDeviceRemoved(deviceId: Int) {
    checkConnectedControllers()
}

override fun onInputDeviceChanged(deviceId: Int) {
    checkConnectedControllers()
}

private fun checkConnectedControllers() {
    val deviceIds = InputDevice.getDeviceIds()
    var controllerNameText = "Sin controles conectados"

    for (id in deviceIds) {
        val device = InputDevice.getDevice(id)
        val sources = device.sources
        
        // Verificar si el dispositivo es un Gamepad
        if ((sources and InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD ||
            (sources and InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK) {
            
            // Aplicar mapeo automático
            val profile = ControllerAutoMapper.detectAndMapController(device)
            controllerNameText = "Conectado: ${profile.name}"
            
            // TODO: Actualizar aquí el TextView de la parte superior del menú
            break
        }
    }
    
    // Ejemplo de actualización en UI (asegúrate de tener tu vista referenciada)
    // binding.txtControllerStatus.text = controllerNameText
}
