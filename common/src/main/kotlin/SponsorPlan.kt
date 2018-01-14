package io.github.droidkaigi.confsched2018.model

data class SponsorPlan(
        var name: String,
        var type: Type,
        var groups: List<SponsorGroup>
) {
    sealed class Type {
        object Platinum : Type()
        object Gold : Type()
        object Silver : Type()
        object Supporter : Type()
        object TechnicalForNetwork : Type()
    }
}
