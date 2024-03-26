import di.apiModule
import di.shareMainModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(apiModule,shareMainModule)
    }
}