package io.github.droidkaigi.confsched2018.model

sealed class Level(
        open val id: Int,
        open val name: String
) {
    data class Beginner(override val id: Int, override val name: String) : Level(id, name)
    data class IntermediateOrExpert(override val id: Int, override val name: String) : Level(id, name)
    data class Niche(override val id: Int, override val name: String) : Level(id, name)

    fun getNameByLang(lang: Lang): String = name
            .split(" / ")
            .getOrElse(lang.ordinal, { name })
            .trim()

    companion object {
        fun of(id: Int, name: String): Level = when (id) {
            3540 -> Beginner(id, name)
            3541 -> IntermediateOrExpert(id, name)
            3542 -> Niche(id, name)
            else -> throw NotImplementedError()
        }
    }
}
