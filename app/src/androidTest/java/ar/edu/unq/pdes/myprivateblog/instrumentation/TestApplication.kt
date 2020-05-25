package ar.edu.unq.pdes.myprivateblog.instrumentation

import ar.edu.unq.pdes.myprivateblog.BaseApplication
import ar.edu.unq.pdes.myprivateblog.BuildConfig
import ar.edu.unq.pdes.myprivateblog.DaggerTestApplicationComponent
import ar.edu.unq.pdes.myprivateblog.di.DaggerApplicationComponent
import timber.log.Timber

class TestApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()

        DaggerTestApplicationComponent.factory()
            .create(applicationContext)
            .inject(this)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}