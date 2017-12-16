package io.github.droidkaigi.confsched2018.data.api.response.mapper

import com.google.gson.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.lang.reflect.Type

class LocalDateTimeSerializer : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDateTime {
        return LocalDateTime.parse(json.getAsString(), formatter)
    }

    override fun serialize(src: LocalDateTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.format(formatter))
    }
}
