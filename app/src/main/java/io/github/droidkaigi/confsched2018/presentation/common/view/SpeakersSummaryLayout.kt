package io.github.droidkaigi.confsched2018.presentation.common.view

import android.content.Context
import android.content.res.TypedArray
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.model.Speaker
import io.github.droidkaigi.confsched2018.presentation.speaker.SpeakerDetailActivity

/**
 * A custom view for showing the avatar icons and names of speakers.
 *
 * ref: https://github.com/DroidKaigi/conference-app-2018/issues/61
 */
class SpeakersSummaryLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    data class CustomAttributes(val textColor: Int)

    private val speakerList: ArrayList<Speaker> = ArrayList()
    private val speakerContainer: RecyclerView
    private val customAttributes: CustomAttributes
    private var speakerIdInDetail: String? = null

    private fun customAttributesFrom(context: Context, attrs: AttributeSet?): CustomAttributes {
        val defaultAttributes = CustomAttributes(
                textColor = Color.BLACK
        )

        return attrs?.let {
            val a: TypedArray = context.theme.obtainStyledAttributes(attrs,
                    R.styleable.SpeakersSummaryLayout, 0, 0)
            val textColor = a.getColor(R.styleable.SpeakersSummaryLayout_textColor,
                    defaultAttributes.textColor)
            a.recycle()

            return CustomAttributes(
                    textColor = textColor
            )
        } ?: defaultAttributes
    }

    init {
        customAttributes = customAttributesFrom(context, attrs)
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        LayoutInflater.from(context).inflate(R.layout.view_speakers_summary_layout, this)
        speakerContainer = findViewById(R.id.speaker_container)

        speakerContainer.layoutManager = LinearLayoutManager(context)
        setSpeakers(speakerList)
    }

    /**
     * set Speaker's icons and names.
     *
     * Speakers can be set not only by setting this method explicitly,
     * but also by using data-binding like this:
     *
     *   <SpeakerSummaryLayout
     *     android:layout_width="wrap_content"
     *     android:layout_height="wrap_content"
     *     app:speakers="@{speakers}"
     *     />
     */
    fun setSpeakers(speakers: List<Speaker>?) {
        speakers ?: return
        if (speakerList == speakers) return
        speakerList.clear()
        speakerList.addAll(speakers)

        updateSpeakers()
    }

    fun setSpeakerIdInDetail(speakerId: String?) {
        speakerIdInDetail = speakerId
    }

    private fun updateSpeakers() {
        if (speakerList.isEmpty()) return

        val speakerAdapter = SpeakersAdapter(context, speakerList, customAttributes.textColor)
        speakerContainer.adapter = speakerAdapter

        speakerAdapter.onSpeakerClick = { view, speakerId ->
            if (speakerIdInDetail == null ||
                    (speakerIdInDetail != null && !speakerIdInDetail.equals(speakerId))) {
                val sharedElement = Pair(
                        view.findViewById<View>(R.id.speaker_image),
                        speakerId)
                SpeakerDetailActivity.start(
                        activity = context as AppCompatActivity,
                        sharedElement = sharedElement,
                        speakerId = speakerId)
            }
        }
    }
}
