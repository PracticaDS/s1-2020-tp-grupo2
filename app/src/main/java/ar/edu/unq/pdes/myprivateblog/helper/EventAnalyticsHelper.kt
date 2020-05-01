package ar.edu.unq.pdes.myprivateblog.helper

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

private fun bundleButton(nameButton: String): Bundle{
    val bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, nameButton)
    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button")
    return bundle;
}

private fun logEvent(context: Context, bundle: Bundle, nameEvent: String){
    val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    firebaseAnalytics.logEvent(nameEvent, bundle)
}

fun logEventCreateNewPost(context: Context){
    val bundle = bundleButton("create_new_post")
    logEvent(context, bundle, "create_new_post")
}

fun logEventSavePost(context: Context){
    val bundle = bundleButton("btn_save")
    logEvent(context, bundle,"save_post")
}

fun logEventEditPost(context: Context){
    val bundle = bundleButton("btn_edit")
    logEvent(context, bundle,"edit_post")
}

fun logEventDeletePost(context: Context){
    val bundle = bundleButton("btn_delete")
    logEvent(context, bundle,"delete_post")
}