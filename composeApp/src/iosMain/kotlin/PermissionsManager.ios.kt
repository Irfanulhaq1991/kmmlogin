import androidx.compose.runtime.Composable

actual class PermissionManger actual constructor(callback: PermissionCallback) : PermissionHandler {


    @Composable
    override fun askPermission(permission: PermissionType) {
        TODO("Not yet implemented")
    }
    @Composable
    override fun isPermissionGranted(permission: PermissionType): Boolean {
        TODO("Not yet implemented")
    }
    @Composable
    override fun launchSettings() {
        TODO("Not yet implemented")
    }
}

@Composable
actual fun createPermissionManger(callback: PermissionCallback): PermissionManger {
    TODO("Not yet implemented")
}