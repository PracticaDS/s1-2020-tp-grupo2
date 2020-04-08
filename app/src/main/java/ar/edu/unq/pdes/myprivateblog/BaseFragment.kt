package ar.edu.unq.pdes.myprivateblog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    abstract val layoutId: Int

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    fun getActivityViewModel(): MainActivityViewModel = (activity as MainActivity).viewModel

    fun getMainActivity(): MainActivity = (activity as MainActivity)

    protected fun applyStatusBarStyle(backgroundColor: Int, lumaThreshold: Float = 0.7f) {
        val window = getMainActivity().window
        val brightness = ColorUtils.luminance(backgroundColor)

        if (brightness > lumaThreshold) {
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility = 0 // clear all flags
        }

        window.statusBarColor = backgroundColor
    }
}

object ColorUtils {
    fun luminance(@ColorInt color: Int): Float = saturate(
        0.2126f * Color.red(color) / 255f + 0.7152f * Color.green(color) / 255f + 0.0722f * Color.blue(
            color
        ) / 255f
    )

    private fun saturate(v: Float): Float = if (v <= 0.0f) 0.0f else if (v >= 1.0f) 1.0f else v
}