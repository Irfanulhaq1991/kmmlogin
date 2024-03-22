import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap


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

interface PhotoManagerManager{
    suspend fun getGalleryPhoto():ImageBitmap
    suspend fun getCameraPhoto():ImageBitmap
}

@Composable
expect fun rememberPermissionManager():PermissionsManager

@Composable
expect fun rememberPhotoManager():PhotoManagerManager


