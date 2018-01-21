package io.github.droidkaigi.confsched2018.data.local

import android.content.Context
import java.io.IOException

object LocalJsonParser {
    @Throws(IOException::class)
    fun loadJsonFromAsset(context: Context, fileName: String): String {
        val file = context.assets.open(fileName)
        val buffer = ByteArray(file.available())
        file.read(buffer)
        file.close()
        return String(buffer)
    }
}
