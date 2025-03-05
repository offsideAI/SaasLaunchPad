package app.saaslaunchpad.saaslaunchpadapp.config

/**
 * Configuration class for managing feature settings across the application.
 */
object FeatureConfiguration {
    /**
     * Enum representing different authentication backend options.
     */
    enum class AuthBackend {
        FIREBASE,
        DJANGO;

        companion object {
            /**
             * The currently active authentication backend.
             * Change this value to switch between different authentication backends.
             */
            val ACTIVE: AuthBackend = FIREBASE
        }
    }

    /**
     * Checks if Firebase authentication is enabled.
     */
    fun isFirebaseAuthEnabled(): Boolean = AuthBackend.ACTIVE == AuthBackend.FIREBASE

    /**
     * Checks if Django authentication is enabled.
     */
    fun isDjangoAuthEnabled(): Boolean = AuthBackend.ACTIVE == AuthBackend.DJANGO
}
