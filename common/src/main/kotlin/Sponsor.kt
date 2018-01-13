package io.github.droidkaigi.confsched2018.model

data class Sponsor(
        var link: String,

        var imageUri: ImageUri
) {
    sealed class ImageUri(val uri: String) {
        class Base64ImageUri(uri: String) : ImageUri(uri)
        class NetworkImageUri(uri: String) : ImageUri(uri)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false
            return this.uri.equals((other as ImageUri).uri)
        }

        override fun hashCode(): Int = uri.hashCode()
    }
}
