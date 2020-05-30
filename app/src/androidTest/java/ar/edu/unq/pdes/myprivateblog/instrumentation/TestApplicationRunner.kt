package ar.edu.unq.pdes.myprivateblog.instrumentation

import android.app.Instrumentation
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class TestApplicationRunner: AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?)
            = Instrumentation.newApplication(TestApplication::class.java, context)
}