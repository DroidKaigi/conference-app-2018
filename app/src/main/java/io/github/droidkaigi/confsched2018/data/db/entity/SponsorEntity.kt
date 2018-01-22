package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "sponsor",
        foreignKeys = [
            ForeignKey(
                    entity = SponsorPlanEntity::class,
                    parentColumns = arrayOf("id"),
                    childColumns = arrayOf("planId"),
                    onDelete = ForeignKey.CASCADE
            )
        ])
data class SponsorEntity(
        @PrimaryKey
        var planId: Int,
        var link: String,
        @ColumnInfo(name = "base64_img")
        var base64Img: String?,
        @ColumnInfo(name = "img_url")
        var imgUrl: String?
)
