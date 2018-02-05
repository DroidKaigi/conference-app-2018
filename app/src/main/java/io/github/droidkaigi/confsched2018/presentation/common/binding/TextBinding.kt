package io.github.droidkaigi.confsched2018.presentation.common.binding

import android.app.SearchManager
import android.content.Intent
import android.databinding.BindingAdapter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.StyleSpan
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.util.ext.isLayoutDirectionRtl
import io.github.droidkaigi.confsched2018.util.ext.selectedText
import io.github.droidkaigi.confsched2018.util.ext.toReadableDateTimeString
import io.github.droidkaigi.confsched2018.util.ext.toReadableTimeString
import java.util.Date
import java.util.regex.Pattern

@BindingAdapter(value = ["dayNumber"])
fun TextView.setDayText(dayNumber: Int) {
    text = context.getString(
            R.string.session_day_title,
            dayNumber
    )
}

@BindingAdapter(value = ["startDate", "endDate"])
fun TextView.setPeriodText(startDate: Date?, endDate: Date?) {
    startDate ?: return
    endDate ?: return
    text = context.getString(
            R.string.time_period,
            startDate.toReadableTimeString(),
            endDate.toReadableTimeString()
    )
}

@BindingAdapter(value = ["prefix", "roomName"])
fun TextView.setRoomText(prefix: String?, roomName: String?) {
    prefix ?: return
    roomName ?: return
    text = if (isLayoutDirectionRtl()) {
        context.getString(R.string.room_format_rtl, prefix, roomName)
    } else {
        context.getString(R.string.room_format, prefix, roomName)
    }
}

@BindingAdapter(value = ["android:text"])
fun TextView.setDateText(date: Date?) {
    date ?: return
    text = date.toReadableDateTimeString()
}

@BindingAdapter(value = ["highlightText"])
fun TextView.setHighlightText(highlightText: String?) {
    // By toString, clear highlight text.
    val stringBuilder = SpannableStringBuilder(text.toString())
    if (highlightText.isNullOrEmpty()) {
        text = stringBuilder
        return
    }
    val pattern = Pattern.compile(highlightText, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(text)
    while (matcher.find()) {
        stringBuilder.setSpan(
                BackgroundColorSpan(ContextCompat.getColor(context, R.color.highlight_text)),
                matcher.start(),
                matcher.end(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        stringBuilder.setSpan(
                StyleSpan(Typeface.BOLD),
                matcher.start(),
                matcher.end(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
    }
    text = stringBuilder
}

@BindingAdapter(value = ["vectorDrawableStart"])
fun TextView.setVectorDrawableStart(drawable: Drawable) {
    val vectorDrawable = drawable.takeIf {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    } ?: StateListDrawable().apply {
        addState(intArrayOf(), drawable)
    }

    val drawables = compoundDrawables
    setCompoundDrawablesWithIntrinsicBounds(
            vectorDrawable,
            drawables[1],
            drawables[2],
            drawables[3]
    )
}

@BindingAdapter(value = ["android:textIsSelectable", "menuWebSearchEnabled"])
fun TextView.setTextIsSelectable(selectable: Boolean, menuWebSearchEnabled: Boolean) {
    setTextIsSelectable(selectable)

    if (selectable) {
        customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return when {
                    menuWebSearchEnabled && item?.itemId == R.id.text_view_menu_web_search -> {
                        Intent(Intent.ACTION_WEB_SEARCH).apply {
                            putExtra(SearchManager.QUERY, selectedText.toString())
                            context.startActivity(this)
                        }
                        true
                    }
                    else -> false
                }
            }

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                if (menuWebSearchEnabled) {
                    menu?.add(Menu.NONE, R.id.text_view_menu_web_search,
                            Menu.NONE, R.string.web_search)
                }

                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
            }
        }
    } else {
        customSelectionActionModeCallback = null
    }
}
