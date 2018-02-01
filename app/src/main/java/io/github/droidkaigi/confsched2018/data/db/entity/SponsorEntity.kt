package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "sponsor")
data class SponsorEntity(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        @ColumnInfo(name = "group_index")
        var groupIndex: Int,
        @Embedded
        var plan: SponsorPlanEntity,
        var link: String,
        @ColumnInfo(name = "base64_img")
        var base64Img: String?,
        @ColumnInfo(name = "img_url")
        var imgUrl: String?
)
