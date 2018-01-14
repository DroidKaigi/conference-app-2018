package io.github.droidkaigi.confsched2018.data.api.response

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class SponsorPlan(
        @Json(name = "plan_name")
        var planName: String,
        @Json(name = "plan_type")
        var planType: Type,
        var groups: List<SponsorGroup>
) {
    enum class Type {
        PLATINUM,
        GOLD,
        SILVER,
        SUPPORTER,
        TECHNICAL_FOR_NETWORK
    }
}
