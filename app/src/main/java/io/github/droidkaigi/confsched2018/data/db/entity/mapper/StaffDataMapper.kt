package io.github.droidkaigi.confsched2018.data.db.entity.mapper

import io.github.droidkaigi.confsched2018.data.db.entity.StaffEntity
import io.github.droidkaigi.confsched2018.model.Staff

fun StaffEntity.toStaff(): Staff = Staff(
        name = name,
        avatarUrl = avatarUrl,
        htmlUrl = htmlUrl
)

fun List<StaffEntity>.toStaff(): List<Staff> = map {
    it.toStaff()
}
