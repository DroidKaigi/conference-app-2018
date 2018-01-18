package io.github.droidkaigi.confsched2018.data.api.response

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class Sponsor(
        var link: String,

        /**
         * base64 encoded image. imgUrl is null if this exists.
         */
        @Json(name = "base64_img")
        var base64Img: String?,

        /**
         * image url. base64Img is null if this exists.
         */
        @Json(name = "img_url")
        var imgUrl: String?
)
