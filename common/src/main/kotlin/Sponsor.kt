package io.github.droidkaigi.confsched2018.model

data class Sponsor(
        var link: String,

        /**
         * base64 encoded image. imgUrl is null if this exists.
         */
        var base64Img: String?,

        /**
         * image url. base64Img is null if this exists.
         */
        var imgUrl: String?
)
