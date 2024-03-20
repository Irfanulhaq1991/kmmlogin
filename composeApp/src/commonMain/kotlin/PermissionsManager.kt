import androidx.compose.runtime.Composable


enum class PermissionStatus{
    GRANTED, DENIED, SHOW_RATIONAL
}
enum class PermissionType{
    CAMERA, GALLERY
}

interface PermissionHandler{
    @Composable
    fun askPermission(permission: PermissionType)

    @Composable
    fun isPermissionGranted(permission: PermissionType): Boolean

    @Composable
    fun launchSettings()

}


interface PermissionCallback{
    fun onPermissionStatus(permission: PermissionType, status: PermissionStatus)
}

expect class PermissionManger(callback: PermissionCallback):PermissionHandler

@Composable
expect fun createPermissionManger(callback: PermissionCallback):PermissionManger




