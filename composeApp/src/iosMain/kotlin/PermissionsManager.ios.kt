import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.AVFoundation.AVAuthorizationStatus
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Foundation.NSURL
import platform.Photos.PHAuthorizationStatus
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

actual class PermissionManger actual constructor(private val callback: PermissionCallback) :
    PermissionHandler {

    @Composable
    override fun askPermission(permission: PermissionType) {
        when (permission) {
            PermissionType.CAMERA -> {
                val permissionStatus: AVAuthorizationStatus =
                    remember { AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo) }
                askCameraPermission(permissionStatus, permission)
            }

            PermissionType.GALLERY -> {
                val permissionStatus: PHAuthorizationStatus =
                    remember { PHPhotoLibrary.authorizationStatus() }
                askGalleryPermission(permissionStatus, permission)
            }
        }
    }

    private fun askGalleryPermission(
        permissionStatus: PHAuthorizationStatus,
        permission: PermissionType
    ) {
        when (permissionStatus) {
            PHAuthorizationStatusAuthorized -> {
                callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
            }

            PHAuthorizationStatusDenied -> {
                callback.onPermissionStatus(permission, PermissionStatus.DENIED)

            }

            PHAuthorizationStatusNotDetermined -> {
                PHPhotoLibrary.requestAuthorization { newStatus ->
                    askGalleryPermission(newStatus, permission)
                }
            }
        }
    }

    @Composable
    override fun isPermissionGranted(permission: PermissionType): Boolean {
        return when (permission) {
            PermissionType.CAMERA -> {
                val permissionStatus: AVAuthorizationStatus =
                    remember { AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo) }
                permissionStatus == AVAuthorizationStatusAuthorized

            }

            PermissionType.GALLERY -> {
                val permissionStatus: PHAuthorizationStatus =
                    remember { PHPhotoLibrary.authorizationStatus() }
                permissionStatus == PHAuthorizationStatusAuthorized
            }
        }
    }

    @Composable
    override fun launchSettings() {
        NSURL.URLWithString(UIApplicationOpenSettingsURLString)?.let {
            UIApplication.sharedApplication.openURL(it)
        }
    }

    private fun askCameraPermission(
        permissionStatus: AVAuthorizationStatus,
        permission: PermissionType
    ) {
        when (permissionStatus) {
            AVAuthorizationStatusAuthorized -> {
                callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
            }

            AVAuthorizationStatusDenied -> {
                callback.onPermissionStatus(permission, PermissionStatus.DENIED)
            }

            AVAuthorizationStatusNotDetermined -> {
                AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { isGranted ->
                    if (isGranted)
                        callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
                    else
                        callback.onPermissionStatus(permission, PermissionStatus.DENIED)


                }
            }
        }
    }
}


actual fun createPermissionManger(callback: PermissionCallback): PermissionManger {
    return PermissionManger(callback)
}