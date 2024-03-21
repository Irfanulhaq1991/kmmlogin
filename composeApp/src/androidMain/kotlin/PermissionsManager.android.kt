import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import org.irfan.project.PermissionFragment
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val permissionFragmentTag = "permissionFragmentTag"

internal class PermissionMangerAndroidImpl(
    private val context: Context,
) : PermissionsManager {
    override suspend fun askPermission(permission: PermissionType): PermissionStatus {
        val fragmentManager = (context as FragmentActivity).supportFragmentManager
        val currentFrag: Fragment? = fragmentManager.findFragmentByTag(permissionFragmentTag)
        val permissionFrag =
            if (currentFrag != null)
                currentFrag as PermissionFragment
            else
                PermissionFragment().also {
                    fragmentManager
                        .beginTransaction()
                        .add(it, permissionFragmentTag)
                        .commit()
                }

        val manifestPermission =  when (permission) {
            PermissionType.CAMERA -> {
                Manifest.permission.CAMERA
            }
            PermissionType.GALLERY -> {
               Manifest.permission.CAMERA
            }
        }
        return suspendCoroutine {continuation->
            permissionFrag.requestPermission(manifestPermission){
                continuation.resume(it)
            }
        }
    }

    override suspend fun isPermissionGranted(permission: PermissionType) = when (permission) {
        PermissionType.CAMERA -> {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        }

        PermissionType.GALLERY -> {
            true
        }
    }


    override fun launchSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        ).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
            .also { context.applicationContext.startActivity(it) }
    }
}


internal class PhotoManagerAndroidImpl(private val context: Context):PhotoManagerManager{
    override suspend fun getGalleryPhoto(): ImageBitmap? {
        val fragmentManager = (context as FragmentActivity).supportFragmentManager
        val currentFrag: Fragment? = fragmentManager.findFragmentByTag(permissionFragmentTag)
        val permissionFrag =
            if (currentFrag != null)
                currentFrag as PermissionFragment
            else
                PermissionFragment().also {
                    fragmentManager
                        .beginTransaction()
                        .add(it, permissionFragmentTag)
                        .commit()
                }

        return suspendCoroutine {continuation->
            permissionFrag.getGalleryImage {
                continuation.resume(it)
            }
        }
    }

    override suspend fun getCameraPhoto(): ImageBitmap? {
        TODO("Not yet implemented")
    }

}
@Composable
actual fun rememberPermissionManager(): PermissionsManager {
    val context = LocalContext.current
    return PermissionMangerAndroidImpl(context)
}

@Composable
actual fun rememberPhotoManager(): PhotoManagerManager {
    val context = LocalContext.current
    return PhotoManagerAndroidImpl(context)
}