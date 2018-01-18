package io.github.droidkaigi.confsched2018.model

data class Sponsor(
        var link: String,

        var imageUri: ImageUri
) {
    sealed class ImageUri {
        abstract val uri: String

        data class Base64ImageUri(override val uri: String) : ImageUri()
        data class NetworkImageUri(override val uri: String) : ImageUri()
    }
}
