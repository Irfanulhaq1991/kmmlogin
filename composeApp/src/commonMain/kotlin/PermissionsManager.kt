import androidx.compose.runtime.Composable


enum class PermissionStatus{
    GRANTED, DENIED, SHOW_RATIONAL
}
enum class PermissionType{
    CAMERA, GALLERY
}

interface PermissionsManager{
   suspend fun askPermission(permission: PermissionType):PermissionStatus
   suspend fun isPermissionGranted(permission: PermissionType): Boolean
   fun launchSettings()
}

@Composable
expect fun rememberPermissionManager():PermissionsManager




