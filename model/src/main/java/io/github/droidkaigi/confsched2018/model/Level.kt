package io.github.droidkaigi.confsched2018.model

sealed class Level(
        val id: Int,
        open val name: String
) {
    data class Beginner(override val name: String) : Level(BEGINNER, name)
    data class IntermediateOrExpert(override val name: String) : Level(INTERMEDIATE_OR_EXPERT, name)
    data class Niche(override val name: String) : Level(NICHE, name)

    fun getNameByLang(lang: Lang): String = name
            .split(" / ")
            .getOrElse(lang.ordinal, { name })
            .trim()

    companion object {
        const val BEGINNER = 3540
        const val INTERMEDIATE_OR_EXPERT = 3541
        const val NICHE = 3542

        fun of(id: Int, name: String): Level = when (id) {
            BEGINNER -> Beginner(name)
            INTERMEDIATE_OR_EXPERT -> IntermediateOrExpert(name)
            NICHE -> Niche(name)
            else -> throw NotImplementedError()
        }
    }
}
