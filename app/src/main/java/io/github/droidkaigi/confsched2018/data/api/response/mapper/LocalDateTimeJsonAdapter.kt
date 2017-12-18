package io.github.droidkaigi.confsched2018.data.api.response.mapper

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class LocalDateTimeJsonAdapter : JsonAdapter<LocalDateTime>() {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    override fun toJson(writer: JsonWriter, value: LocalDateTime?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value.format(formatter))
        }
    }

    override fun fromJson(reader: JsonReader): LocalDateTime? = when (reader.peek()) {
        JsonReader.Token.NULL -> reader.nextNull()
        else -> LocalDateTime.parse(reader.nextString(), formatter)
    }

}
