package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "sponsor_group",
        foreignKeys = [
           (ForeignKey(
                entity = SponsorPlanEntity::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("plan_id"),
                onDelete = ForeignKey.CASCADE
        ))])
data class SponsorGroupEntity(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        @ColumnInfo(name = "plan_id", index = true)
        var planId: Int
)
