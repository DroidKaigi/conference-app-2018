package io.github.droidkaigi.confsched2018.model

sealed class AboutThisApp(
        open val id: Int,
        open val name: Int,
        open val description: Int,
        open val navigationUrl: String?
) {
    data class Item(
            override val id: Int,
            override val name: Int,
            override val description: Int,
            override val navigationUrl: String?
    ) : AboutThisApp(id, name, description, navigationUrl)

    data class HeadItem(
            override val id: Int,
            override val name: Int,
            override val description: Int,
            override val navigationUrl: String?,
            val faceBookUrl: String,
            val twitterUrl: String,
            val githubUrl: String,
            val youtubeUrl: String,
            val mediumUrl: String
    ) : AboutThisApp(id, name, description, navigationUrl)

}
