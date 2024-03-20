import di.KoinHelper
import di.shareMainModule
import org.koin.test.check.checkModules
import kotlin.test.Test

class DiTest {
    @Test
    fun checkAllModules() = checkModules {
        modules(shareMainModule)
    }
}