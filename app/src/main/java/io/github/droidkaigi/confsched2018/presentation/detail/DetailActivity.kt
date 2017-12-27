package io.github.droidkaigi.confsched2018.presentation.detail

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSessionBinding

class DetailActivity : AppCompatActivity() {
    private val binding: ItemSessionBinding by lazy {
        DataBindingUtil.setContentView<ItemSessionBinding>(this, R.layout.item_session)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
        // TODO: create layout
//        setSupportActionBar(binding.toolbar)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, DetailActivity::class.java))
        }
    }
}
