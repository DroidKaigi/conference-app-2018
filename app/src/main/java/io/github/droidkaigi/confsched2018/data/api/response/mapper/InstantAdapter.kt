package io.github.droidkaigi.confsched2018.data.api.response.mapper

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import io.github.droidkaigi.confsched2018.util.ext.atJST
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class InstantAdapter : JsonAdapter<Instant>() {
    override fun toJson(writer: JsonWriter, value: Instant?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value.atJST().format(FORMATTER))
        }
    }

    override fun fromJson(reader: JsonReader): Instant? = when (reader.peek()) {
        JsonReader.Token.NULL -> reader.nextNull()
        else -> {
            val dateString = reader.nextString()
            parseDateString(dateString)
        }
    }

    companion object {
        private val FORMATTER: DateTimeFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

        /**
         * Obtains an instance of Instant from a text string such as "2018-02-08T17:40:00".
         * The string must represent in JST (GMT+9:00).
         */
        fun parseDateString(dateString: String): Instant =
                LocalDateTime.parse(dateString, FORMATTER).atJST().toInstant()
    }
}
