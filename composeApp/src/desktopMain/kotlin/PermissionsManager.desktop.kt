import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image.Companion.makeFromEncoded
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class PermissionManagerDesktopImpl:PermissionsManager{
    override suspend fun askPermission(permission: PermissionType): PermissionStatus {
      return PermissionStatus.GRANTED
    }

    override suspend fun isPermissionGranted(permission: PermissionType): Boolean {
        return true
    }

    override fun launchSettings() {

    }

}
internal class PhotoManagerDesktopImpl:PhotoManagerManager{
    private fun photoFromFile(file: File): ImageBitmap {
        val photoBytes  = file.readBytes()
        return makeFromEncoded(photoBytes).toComposeImageBitmap()
    }

    override suspend fun getGalleryPhoto(): ImageBitmap {

       return  suspendCoroutine { continuation ->
           try {
               val dialog = FileDialog(null as Frame?, "Select File to Open")
               dialog.mode = FileDialog.LOAD
               dialog.isVisible = true
               val file: String = dialog.file
               val dir: String = dialog.directory
               val imageBitmap = photoFromFile(File("$dir/$file"))
               continuation.resume(imageBitmap)
           }catch (e:Exception){
              throw Exception("")
           }
       }
    }

    override suspend fun getCameraPhoto(): ImageBitmap {
      throw Exception("Camera is not supported")
    }

}
@Composable
actual fun rememberPhotoManager(): PhotoManagerManager {
    return PhotoManagerDesktopImpl()
}

@Composable
actual fun rememberPermissionManager(): PermissionsManager {
    return PermissionManagerDesktopImpl()
}