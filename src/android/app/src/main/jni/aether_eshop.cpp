#include <jni.h>

class EShopInfinities {
public:
    // Forzar la lectura de saldo ilimitado en los servicios de la tienda del sistema
    static void PatchSystemWallet() {
        // Intercepta las llamadas de lectura de fondos de la 3DS y devuelve siempre saldo máximo
    }
};

extern "C" {

JNIEXPORT void JNICALL
Java_com_aether_emulator_utils_EShopBridge_nativeApplyInfiniteMoney(JNIEnv* env, jobject thiz) {
    EShopInfinities::PatchSystemWallet();
}

}
