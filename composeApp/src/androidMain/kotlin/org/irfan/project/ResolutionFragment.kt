package org.irfan.project

import PermissionStatus
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import org.irfan.kmm_login.BuildConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

internal class ResolutionFragment : Fragment() {

    private var perMissionCallback: (status: PermissionStatus) -> Unit = {}
    private var galleryCallBack: (bitmap: ImageBitmap) -> Unit = {}
    private var cameraCallBack: (bitmap: ImageBitmap) -> Unit = {}
    private val uri: Uri by lazy {
        val file = requireContext().createImageFile()
        FileProvider.getUriForFile(
            requireContext(),
            BuildConfig.APPLICATION_ID + ".provider", file
        )

    }

    private val permissionRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                perMissionCallback(PermissionStatus.GRANTED)
            else
                perMissionCallback(PermissionStatus.DENIED)
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val imageBytes = uri?.let {
                requireContext().contentResolver.openInputStream(uri)?.use { it.readBytes() }
            } ?: throw Exception("Could not select photo")

            val bitmap =
                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).asImageBitmap()
            galleryCallBack(bitmap)

        }


    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            val imageBytes =
                requireContext().contentResolver.openInputStream(uri)?.use { it.readBytes()
                } ?: throw Exception("Could not select photo")
                val bitmap =
                    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).asImageBitmap()
                cameraCallBack(bitmap)
        }


    fun requestPermission(
        permission: String,
        perMissionCallback: (status: PermissionStatus) -> Unit
    ) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        permission
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    perMissionCallback(PermissionStatus.GRANTED)
                } else {
                    this@ResolutionFragment.perMissionCallback = perMissionCallback
                    permissionRequestLauncher.launch(permission)
                }
            }
        }
    }

    fun getGalleryImage(galleryCallBack: (bitmap: ImageBitmap) -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                this@ResolutionFragment.galleryCallBack = galleryCallBack
                galleryLauncher.launch("image/*")
            }
        }
    }

    fun getCameraImage(cameraCallBack: (bitmap: ImageBitmap) -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                this@ResolutionFragment.cameraCallBack = cameraCallBack
                cameraLauncher.launch(uri)
            }
        }
    }

}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
}