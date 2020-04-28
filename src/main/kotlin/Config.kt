import kotlinx.serialization.Serializable

@Serializable
public data class Config(
    public var pauseAuto: Boolean = true,
    public var pausePermission: Permission = Permission.ADMIN_ONLY
)
