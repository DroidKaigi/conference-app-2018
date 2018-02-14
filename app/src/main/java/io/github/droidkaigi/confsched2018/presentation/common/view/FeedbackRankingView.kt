package io.github.droidkaigi.confsched2018.presentation.common.view

import android.content.Context
import android.databinding.DataBindingUtil
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ViewFeedbackRankingBinding
import io.github.droidkaigi.confsched2018.util.ext.toVisible

class FeedbackRankingView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    data class CustomAttribute(
            val maxSize: Int,
            val labelMin: String,
            val labelMax: String)

    private val DEFAULT_MAX_SIZE = 5

    private val customAttribute: CustomAttribute
    private var currentRanking: Int = 0
    private val binding: ViewFeedbackRankingBinding = DataBindingUtil
            .inflate(LayoutInflater.from(context), R.layout.view_feedback_ranking, this, true)

    private var listener: OnCurrentRankingChangeListener? = null

    init {
        customAttribute = customAttributeFrom(attrs)
        initView()
    }

    private fun customAttributeFrom(attrs: AttributeSet?): CustomAttribute {
        return if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.FeedbackRankingView)
            val maxSize = a.getInteger(R.styleable.FeedbackRankingView_rankingMaxSize,
                    DEFAULT_MAX_SIZE)
            val labelMin = a.getString(R.styleable.FeedbackRankingView_rankingLabelMin)
            val labelMax = a.getString(R.styleable.FeedbackRankingView_rankingLabelMax)
            a.recycle()
            CustomAttribute(maxSize, labelMin, labelMax)
        } else {
            CustomAttribute(DEFAULT_MAX_SIZE, "", "")
        }
    }

    private fun initView() {
        if (!TextUtils.isEmpty(customAttribute.labelMin)) {
            binding.txtLabelStart.toVisible()
            binding.txtLabelStart.text = customAttribute.labelMin
        }

        if (!TextUtils.isEmpty(customAttribute.labelMax)) {
            binding.txtLabelEnd.toVisible()
            binding.txtLabelEnd.text = customAttribute.labelMax
        }

        addRankingViews()
    }

    private fun addRankingViews() {
        for (i in 1..customAttribute.maxSize) {
            val view = LayoutInflater.from(context)
                    .inflate(R.layout.view_feedback_ranking_item, binding.rankingContainer, false)
            val txtRanking = view.findViewById(R.id.txt_ranking) as TextView
            txtRanking.text = i.toString()
            txtRanking.setOnClickListener { v ->
                unselectAll()
                v.isSelected = true
                currentRanking = i
                if (listener != null) {
                    listener!!.onCurrentRankingChange(this, currentRanking)
                }
            }

            val params = LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.WRAP_CONTENT, 1f)
            binding.rankingContainer.addView(view, params)
        }
    }

    private fun unselectAll() {
        var i = 0
        val size = binding.rankingContainer.childCount
        while (i < size) {
            binding.rankingContainer
                    .getChildAt(i)
                    .findViewById<TextView>(R.id.txt_ranking).isSelected = false
            i++
        }
    }

    fun setCurrentRanking(currentRanking: Int) {
        if (this.currentRanking == currentRanking) {
            return
        }

        if (currentRanking <= 0) {
            unselectAll()
        } else if (currentRanking <= binding.rankingContainer.childCount) {
            unselectAll()
            val view = binding.rankingContainer.getChildAt(currentRanking - 1)
            view.isSelected = true
            this.currentRanking = currentRanking
        }
    }

    fun setListener(listener: OnCurrentRankingChangeListener?) {
        this.listener = listener
    }

    interface OnCurrentRankingChangeListener {

        fun onCurrentRankingChange(view: FeedbackRankingView, currentRanking: Int)
    }
}
