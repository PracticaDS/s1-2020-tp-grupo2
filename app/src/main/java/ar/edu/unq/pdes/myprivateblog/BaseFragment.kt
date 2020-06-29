package ar.edu.unq.pdes.myprivateblog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorInt
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment(private val layoutId: Int) : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    fun getActivityViewModel(): MainActivityViewModel = (activity as MainActivity).viewModel

    fun getMainActivity(): MainActivity = (activity as MainActivity)

    protected fun applyStatusBarStyle(backgroundColor: Int, lumaThreshold: Float = 0.7f) {
        val window = getMainActivity().window
        val brightness = ColorUtils.luminance(backgroundColor)
        if (brightness > lumaThreshold) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility = 0 // clear all flags
        }
        window.statusBarColor = backgroundColor
    }

    protected fun closeAndGoBack() = findNavController().navigateUp()

    protected fun exitApplication(){
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    getMainActivity().finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}

object ColorUtils {
    fun findTextColorGivenBackgroundColor(color: Int, lumaThreshold: Float = 0.7f) =
        if (luminance(color) > lumaThreshold) Color.DKGRAY else Color.WHITE

    fun luminance(@ColorInt color: Int): Float = saturate(
        0.2126f * Color.red(color) / 255f + 0.7152f * Color.green(color) / 255f + 0.0722f * Color.blue(
            color
        ) / 255f
    )

    private fun saturate(v: Float): Float = if (v <= 0.0f) 0.0f else if (v >= 1.0f) 1.0f else v
}