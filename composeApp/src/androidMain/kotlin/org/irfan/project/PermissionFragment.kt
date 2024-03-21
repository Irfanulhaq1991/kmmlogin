package org.irfan.project

import PermissionStatus
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

internal class PermissionFragment : Fragment() {

    private var callback: (status: PermissionStatus) -> Unit = {}
    private val permissionRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                callback(PermissionStatus.GRANTED)
            else
                callback(PermissionStatus.DENIED)
        }

    fun requestPermission(permission: String, callback: (status: PermissionStatus) -> Unit) {
         lifecycleScope.launch {
             lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                 if(ActivityCompat.checkSelfPermission(requireContext(),permission) == PackageManager.PERMISSION_GRANTED){
                     callback(PermissionStatus.GRANTED)
                 }else{
                     this@PermissionFragment.callback = callback
                     permissionRequestLauncher.launch(permission)
                 }
             }
         }
    }
}