import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
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
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PermissionMangerIosImpl() : PermissionsManager {

    override suspend fun askPermission(permission: PermissionType) =
        when (permission) {
            PermissionType.CAMERA -> {
                val permissionStatus: AVAuthorizationStatus =
                    AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
                askCameraPermission(permissionStatus)
            }

            PermissionType.GALLERY -> {
                val permissionStatus: PHAuthorizationStatus = PHPhotoLibrary.authorizationStatus()
                askGalleryPermission(permissionStatus)
            }
        }


    override suspend fun isPermissionGranted(permission: PermissionType) =
        when (permission) {
            PermissionType.CAMERA -> {
                val permissionStatus: AVAuthorizationStatus =
                    AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
                permissionStatus == AVAuthorizationStatusAuthorized

            }

            PermissionType.GALLERY -> {
                val permissionStatus: PHAuthorizationStatus = PHPhotoLibrary.authorizationStatus()
                permissionStatus == PHAuthorizationStatusAuthorized
            }
        }


    override fun launchSettings() {
        NSURL.URLWithString(UIApplicationOpenSettingsURLString)?.let {
            UIApplication.sharedApplication.openURL(it)
        }
    }

    private suspend fun askGalleryPermission(
        permissionStatus: PHAuthorizationStatus,
    ): PermissionStatus {
        return when (permissionStatus) {
            PHAuthorizationStatusAuthorized -> {
                PermissionStatus.GRANTED
            }

            PHAuthorizationStatusDenied -> {
                PermissionStatus.DENIED
            }

            PHAuthorizationStatusNotDetermined -> {
                withContext(Dispatchers.Main) {
                    suspendCancellableCoroutine { continuation ->
                        PHPhotoLibrary.requestAuthorization { newStatus ->
                            if (newStatus == PHAuthorizationStatusAuthorized)
                                continuation.resume(PermissionStatus.GRANTED)
                            else
                                continuation.resume(PermissionStatus.DENIED)
                        }
                    }

                }
            }

            else -> {
                throw IllegalStateException("Invalid PermissionStatus")
            }

        }
    }
}


private suspend fun askCameraPermission(
    permissionStatus: AVAuthorizationStatus,
): PermissionStatus {
    return when (permissionStatus) {
        AVAuthorizationStatusAuthorized -> {
            PermissionStatus.GRANTED
        }

        AVAuthorizationStatusDenied -> {
            PermissionStatus.DENIED
        }

        AVAuthorizationStatusNotDetermined -> {
            suspendCoroutine { continuation ->
                AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { isGranted ->
                    if (isGranted)
                        continuation.resume(PermissionStatus.GRANTED)
                    else
                        continuation.resume(PermissionStatus.DENIED)
                }
            }
        }

        else -> {
            throw IllegalStateException("Invalid PermissionStatus")
        }
    }
}


@Composable
actual fun rememberPermissionManager(): PermissionsManager {
    return PermissionMangerIosImpl()
}