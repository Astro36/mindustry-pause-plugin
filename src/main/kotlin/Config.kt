import kotlinx.serialization.Serializable

@Serializable
data class Config(
        var pauseAuto: Boolean = true,
        var pausePermission: Permission = Permission.ADMIN_ONLY
)
