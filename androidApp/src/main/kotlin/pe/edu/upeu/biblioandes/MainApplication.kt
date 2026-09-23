package pe.edu.upeu.biblioandes

import android.app.Application
import pe.edu.upeu.biblioandes.di.initKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
