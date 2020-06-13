package ar.edu.unq.pdes.myprivateblog.services;

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

enum class TypeEventAnalytics(val eventName:String) {

    CREATE_NEW_POST("CREATE_NEW_POST"),
    EDIT_POST("EDIT_POST"),
    DELETE_POST("DELETE_POST"),
    CANCEL_EDIT_POST("CANCEL_EDIT_POST"),
    VIEW_POST("VIEW_POST")
}

interface  AnalyticsService {
    fun logEvent(typeEvent: TypeEventAnalytics)
}

class FakeAnalyticsService : AnalyticsService {
    override fun logEvent(typeEvent: TypeEventAnalytics){}

}

class FirebaseAnalytics @Inject constructor(private val context: Context) : AnalyticsService {

   override fun logEvent(typeEvent: TypeEventAnalytics){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, typeEvent.toString())
        val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        firebaseAnalytics.logEvent(typeEvent.eventName, bundle)
    }
}