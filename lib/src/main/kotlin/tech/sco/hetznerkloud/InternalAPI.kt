package tech.sco.hetznerkloud

@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is internal in Hetzner Kloud library and should not be used. It could be removed or changed without notice.",
)
@Retention(AnnotationRetention.BINARY)
annotation class InternalAPI
