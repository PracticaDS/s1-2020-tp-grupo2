package ar.edu.unq.pdes.myprivateblog.logger

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class AnalyticsLogger @Inject constructor(private val context: Context){
    private fun bundleButton(nameButton: String): Bundle{
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, nameButton)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button")
        return bundle;
    }

    private fun logEvent(bundle: Bundle, nameEvent: String){
        val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        firebaseAnalytics.logEvent(nameEvent, bundle)
    }

    fun logEventCreateNewPost(){
        val bundle = bundleButton("create_new_post")
        logEvent(bundle, "create_new_post")
    }

    fun logEventSavePost(){
        val bundle = bundleButton("btn_save")
        logEvent(bundle,"save_post")
    }

    fun logEventEditPost(){
        val bundle = bundleButton("btn_edit")
        logEvent(bundle,"edit_post")
    }

    fun logEventDeletePost(){
        val bundle = bundleButton("btn_delete")
        logEvent(bundle,"delete_post")
    }

    fun logEventCancelDeletePost(){
        val bundle = bundleButton("btn_delete")
        logEvent(bundle,"cancel_delete_post")
    }
}