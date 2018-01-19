package io.github.droidkaigi.confsched2018.presentation.sponsors

import android.support.v4.app.Fragment
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.model.SponsorPlan
import io.github.droidkaigi.confsched2018.presentation.sponsors.item.EmptySponsorItem
import io.github.droidkaigi.confsched2018.presentation.sponsors.item.SponsorItem
import io.github.droidkaigi.confsched2018.presentation.sponsors.item.SponsorPlanItem
import io.github.droidkaigi.confsched2018.presentation.sponsors.item.getSponsorItemSpanSize

class SponsorsSection : Section() {
    fun updateSponsorPlans(fragment: Fragment, sponsorPlans: List<SponsorPlan>) {
        val items = sponsorPlans.map { plan ->
            Section(SponsorPlanItem(plan)).apply {
                addAll(plan.groups.flatMap {
                    it.sponsors.map {
                        SponsorItem(fragment, plan.type, it)
                    }
                })

                // fill dead spaces by dummy sponsors
                val spanSize = getSponsorItemSpanSize(plan.type, SPONSOR_SCREEN_MAX_SPAN_SIZE)
                val columnSize = SPONSOR_SCREEN_MAX_SPAN_SIZE / spanSize
                val modSize = (groupCount - 1) % columnSize

                if (modSize > 0) {
                    0.until(columnSize - modSize).forEach {
                        add(EmptySponsorItem(plan.type))
                    }
                }
            }
        }

        update(items)
    }
}
