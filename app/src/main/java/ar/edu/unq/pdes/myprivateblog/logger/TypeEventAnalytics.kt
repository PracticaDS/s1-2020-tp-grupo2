package ar.edu.unq.pdes.myprivateblog.logger

enum class TypeEventAnalytics(val eventName:String) {

    CREATE_NEW_POST("CREATE_NEW_POST"),
    EDIT_POST("EDIT_POST"),
    DELETE_POST("DELETE_POST"),
    CANCEL_EDIT_POST("CANCEL_EDIT_POST"),
    VIEW_POST("VIEW_POST")
}