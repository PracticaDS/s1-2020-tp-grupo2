package ar.edu.unq.pdes.myprivateblog.logger

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class AnalyticsLogger @Inject constructor(private val context: Context){

    fun logEvent( typeEvent: TypeEventAnalytics){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, typeEvent.toString())
        val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        firebaseAnalytics.logEvent(typeEvent.toString(), bundle)
    }
}