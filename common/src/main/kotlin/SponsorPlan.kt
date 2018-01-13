package io.github.droidkaigi.confsched2018.model

typealias Group = List<Sponsor>

data class SponsorPlan(
        var name: String,
        var groups: List<Group>
)
