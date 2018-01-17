package io.github.droidkaigi.confsched2018.presentation.common.view

import android.content.Context
import android.graphics.drawable.Animatable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.widget.Checkable
import io.github.droidkaigi.confsched2018.R

class FavoriteButton @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr), Checkable {

    data class CustomAttribute(
            val checked: Boolean,
            val checkedColorTint: Int,
            val uncheckedColorTint: Int)

    private var isChecked: Boolean = false
    private val customAttribute: CustomAttribute

    init {
        customAttribute = customAttributeFrom(attrs)

        isChecked = customAttribute.checked
        updateDrawableWithoutAnimation()
        setOnClickListener(null)
    }

    private fun customAttributeFrom(attrs: AttributeSet?): CustomAttribute {
        val defaultAttribute = CustomAttribute(false, 0, 0)
        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(attrs,
                    R.styleable.FavoriteButton,
                    0, 0)
            val checked = a.getBoolean(R.styleable.FavoriteButton_android_checked,
                    defaultAttribute.checked)
            val checkedColorTint = a.getColor(R.styleable.FavoriteButton_checkedColorTint,
                    defaultAttribute.checkedColorTint)
            val uncheckedColorTint = a.getColor(R.styleable.FavoriteButton_uncheckedColorTint,
                    defaultAttribute.uncheckedColorTint)
            a.recycle()

            return CustomAttribute(checked, checkedColorTint, uncheckedColorTint)
        } else {
            return defaultAttribute
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener({ view ->
            l?.onClick(view)
            toggle()
        })
    }

    override fun setChecked(b: Boolean) {
        if (isChecked != b) {
            isChecked = b
            updateDrawableWithoutAnimation()
        }
    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun toggle() {
        isChecked = !isChecked
        updateDrawable()
    }

    private fun updateDrawableWithoutAnimation() {
        if (isChecked) {
            // TODO: apply checkedColorTint
            setImageResource(R.drawable.ic_favorite_checked)
        } else {
            // TODO: apply uncheckedColorTint
            setImageResource(R.drawable.ic_favorite_unchecked)
        }
    }

    private fun updateDrawable() {
        (drawable as? Animatable)?.let { animatable ->
            if (animatable.isRunning) {
                animatable.stop()
            }
        }
        if (isChecked) {
            // TODO: apply checkedColorTint
            setImageResource(R.drawable.ic_anim_favorite_checking)
        } else {
            // TODO: apply uncheckedColorTint
            setImageResource(R.drawable.ic_anim_favorite_unchecking)
        }
        (drawable as? Animatable)?.start()
    }
}
