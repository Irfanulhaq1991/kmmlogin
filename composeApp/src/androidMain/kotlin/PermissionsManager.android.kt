import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

actual class PermissionManger actual constructor(private val callback: PermissionCallback) :
    PermissionHandler {
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    override fun askPermission(permission: PermissionType) {
        when (permission) {
            PermissionType.CAMERA -> {
                val permissionStatus = rememberPermissionState(Manifest.permission.CAMERA)
                LaunchedEffect(permissionStatus) {
                    val permissionResult = permissionStatus.status
                    if (!permissionResult.isGranted) {
                        if (permissionResult.shouldShowRationale) {
                            callback.onPermissionStatus(permission, PermissionStatus.SHOW_RATIONAL)
                        } else {
                            permissionStatus.launchPermissionRequest()
                        }
                    } else {
                        callback.onPermissionStatus(
                            permission, PermissionStatus.GRANTED
                        )
                    }
                }
            }

            PermissionType.GALLERY -> {
                callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    override fun isPermissionGranted(permission: PermissionType): Boolean {
        return when (permission) {
            PermissionType.CAMERA -> {
                val permissionStatus = rememberPermissionState(Manifest.permission.CAMERA)
                return permissionStatus.status.isGranted
            }

            PermissionType.GALLERY -> {
                true
            }
        }
    }


    @Composable
    override fun launchSettings() {
        val context = LocalContext.current
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        ).also { context.startActivity(it) }
    }
}

actual fun createPermissionManger(callback: PermissionCallback): PermissionManger {
    return PermissionManger(callback)
}