package ar.edu.unq.pdes.myprivateblog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    open fun onBackPressed(): Boolean {
        return false
    }

    fun getActivityViewModel(): MainActivityViewModel = (activity as MainActivity).viewModel

    fun getMainActivity(): MainActivity = (activity as MainActivity)

}