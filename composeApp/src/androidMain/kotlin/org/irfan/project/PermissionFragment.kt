package org.irfan.project

import PermissionStatus
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.icerock.moko.media.BitmapUtils
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream

internal class PermissionFragment : Fragment() {

    private var callback: (status: PermissionStatus) -> Unit = {}
    private var galleryCallBack: (bitmap: ImageBitmap?) -> Unit = {}
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
          val imageBytes =  uri?.let {  requireContext().contentResolver.openInputStream(uri)?.use { it.readBytes() }}
            if(imageBytes != null) {
                val bitmap =
                    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).asImageBitmap()
                galleryCallBack(bitmap)
            }else{
                galleryCallBack(null)
            }
        }


    private val permissionRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                callback(PermissionStatus.GRANTED)
            else
                callback(PermissionStatus.DENIED)
        }

    fun requestPermission(permission: String, callback: (status: PermissionStatus) -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        permission
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    callback(PermissionStatus.GRANTED)
                } else {
                    this@PermissionFragment.callback = callback
                    permissionRequestLauncher.launch(permission)
                }
            }
        }
    }

    fun getGalleryImage(galleryCallBack: (bitmap: ImageBitmap?) -> Unit){
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                this@PermissionFragment.galleryCallBack = galleryCallBack
                galleryLauncher.launch( "image/*")
            }
        }
    }

}