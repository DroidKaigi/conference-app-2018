package io.github.droidkaigi.confsched2018.presentation.common.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.model.Speaker
import io.github.droidkaigi.confsched2018.util.CustomGlideApp
import io.github.droidkaigi.confsched2018.util.ext.toGone
import io.github.droidkaigi.confsched2018.util.ext.toVisible

/**
 * A custom view for showing the avatar icons and names of speakers.
 *
 * ref: https://github.com/DroidKaigi/conference-app-2018/issues/61
 */
class SpeakersSummaryLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    data class CustomAttributes(val maxIcons: Int, val imageSize: Int, val textColor: Int)

    private val speakerList: ArrayList<Speaker> = ArrayList()
    private val customAttributes: CustomAttributes
    private val imageContainer: FrameLayout
    private val textView: TextView

    private fun customAttributesFrom(context: Context, attrs: AttributeSet?): CustomAttributes {
        val res = context.resources
        val defaultAttributes = CustomAttributes(
                maxIcons = res.getInteger(R.integer.default_speakers_summary_layout_max_icon_count),
                imageSize = res.getDimensionPixelSize(R.dimen.speaker_image),
                textColor = 0
        )

        return attrs?.let {
            val a: TypedArray = context.theme.obtainStyledAttributes(attrs,
                    R.styleable.SpeakersSummaryLayout, 0, 0)
            val maxIcons = a.getInt(R.styleable.SpeakersSummaryLayout_max_icon_count,
                    defaultAttributes.maxIcons)
            val imageSize = a.getDimensionPixelSize(R.styleable.SpeakersSummaryLayout_image_size,
                    defaultAttributes.imageSize)
            val textColor = a.getColor(R.styleable.SpeakersSummaryLayout_textColor,
                    defaultAttributes.textColor)
            a.recycle()

            return CustomAttributes(
                    maxIcons = maxIcons,
                    imageSize = imageSize,
                    textColor = textColor
            )
        } ?: defaultAttributes
    }

    init {
        customAttributes = customAttributesFrom(context, attrs)
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        LayoutInflater.from(context).inflate(R.layout.view_spearkers_summary_layout, this)
        imageContainer = findViewById(R.id.speaker_image_container)
        textView = findViewById(R.id.speakers)

        updateIcons()
        updateText()
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
        speakers?.let {
            speakerList.clear()
            speakerList.addAll(speakers)

            updateIcons()
            updateText()
        }
    }

    private fun updateIcons() {
        if (speakerList.isEmpty()) {
            imageContainer.toGone()
        } else {
            imageContainer.removeAllViews()
            speakerList.take(customAttributes.maxIcons).let { speakers ->
                val size = speakers.size
                speakers.reversed().forEachIndexed { index, speaker ->
                    imageContainer.addView(createIconImageView(speaker, size - 1 - index))
                }
            }
            imageContainer.toVisible()
        }
    }

    private fun createIconImageView(speaker: Speaker, index: Int): View {
        return ImageView(context).also { imageView ->
            val imageSize = customAttributes.imageSize
            imageView.contentDescription =
                    context.getString(R.string.session_content_description_speaker)
            imageView.layoutParams = FrameLayout.LayoutParams(imageSize, imageSize).also { lp ->
                lp.marginStart = index * imageSize * 3 / 4
            }
            CustomGlideApp
                    .with(this)
                    .load(speaker.imageUrl)
                    .placeholder(R.drawable.ic_person_black_24dp)
                    .override(imageSize, imageSize)
                    .dontAnimate()
                    .transform(CircleCrop())
                    .into(imageView)
        }
    }

    private fun updateText() {
        if (speakerList.isEmpty()) {
            textView.toGone()
        } else {
            textView.text = speakerList.joinToString { it.name }
            if (customAttributes.textColor != 0) {
                textView.setTextColor(customAttributes.textColor)
            }
            textView.toVisible()
        }
    }
}
