import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Image
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
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject
import platform.posix.memcpy
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class PermissionMangerIosImpl() : PermissionsManager {

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


internal class PhotoManagerIosImpl:PhotoManagerManager{
    @OptIn(ExperimentalForeignApi::class)
    override suspend fun getGalleryPhoto(): ImageBitmap? {
       return suspendCoroutine {continuation->
        val imagePicker = UIImagePickerController()
        val galleryDelegate =
            object : NSObject(), UIImagePickerControllerDelegateProtocol,
                UINavigationControllerDelegateProtocol {
                override fun imagePickerController(
                    picker: UIImagePickerController,
                    didFinishPickingImage: UIImage,
                    editingInfo: Map<Any?, *>?
                ) {

                    val imageNsData = UIImageJPEGRepresentation(didFinishPickingImage, 1.0)
                        ?: return
                    val bytes = ByteArray(imageNsData.length.toInt())
                    memcpy(bytes.refTo(0), imageNsData.bytes, imageNsData.length)
                    val imageBitmap =   Image.makeFromEncoded(bytes).toComposeImageBitmap()
                    continuation.resume(imageBitmap)
                    picker.dismissViewControllerAnimated(true, null)
                }
            }

           imagePicker.setSourceType(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary)
           imagePicker.setDelegate(galleryDelegate)
           UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
               imagePicker, true, null
           )
        }

    }

    override suspend fun getCameraPhoto(): ImageBitmap? {
        TODO("Not yet implemented")
    }

}
@Composable
actual fun rememberPermissionManager(): PermissionsManager {
    return PermissionMangerIosImpl()
}

@Composable
actual fun rememberPhotoManager(): PhotoManagerManager {
    return PhotoManagerIosImpl()
}