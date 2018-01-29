package io.github.droidkaigi.confsched2018.util.ext

import android.app.SearchManager
import android.content.Intent
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import io.github.droidkaigi.confsched2018.R

// Prevent requestLayout()
fun TextView.setTextIfChanged(newText: String) {
    if (newText != text) {
        text = newText
    }
}

val TextView.selectedText: CharSequence
    get() = text.subSequence(selectionStart, selectionEnd)

fun TextView.setTextIsSelectable(selectable: Boolean, withWebSearch: Boolean = false) {
    setTextIsSelectable(selectable)

    if (selectable) {
        customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return when {
                    withWebSearch && item?.itemId == R.id.text_view_menu_web_search -> {
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
                if (withWebSearch) {
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
