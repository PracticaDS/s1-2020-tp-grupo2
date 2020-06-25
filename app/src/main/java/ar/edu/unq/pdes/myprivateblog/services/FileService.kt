package ar.edu.unq.pdes.myprivateblog.services

import android.content.Context
import java.io.File
import java.io.OutputStreamWriter
import java.util.*

class FileService(val context: Context) {

    fun updateBody(bodyPath: String?, bodyText: String) {
        if (bodyPath == null) return
        val outputStreamWriter =
            OutputStreamWriter(context.openFileOutput(bodyPath, Context.MODE_PRIVATE))
        outputStreamWriter.use { it.write(bodyText) }
    }

    fun saveBody(bodyText: String): String {
        val fileName = UUID.randomUUID().toString() + ".body"
        updateBody(fileName, bodyText)
        return fileName
    }

    fun readBody(bodyPath: String?): String {
        if (bodyPath == null) return ""
        val file = File(context.filesDir, bodyPath)
        return if (file.exists()) file.readText() else ""
    }

}