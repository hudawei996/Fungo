package com.fungo.baselib.view.placeholder

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.fungo.baselib.R
import com.fungo.baselib.utils.ViewUtils
import kotlinx.android.synthetic.main.layout_place_holder.view.*

/**
 * @author Pinger
 * @since 18-7-25 下午12:44
 * 占位图容器，默认不展示自己，调用了相对应的方法才会进行展示
 */

class PlaceholderView : FrameLayout {


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr, 0)

    init {
        visibility = View.GONE
        LayoutInflater.from(context).inflate(R.layout.layout_place_holder, null)
    }


    fun showLoading() {
        performViewVisible(true, false, false)
    }


    fun showEmpty() {
        performViewVisible(false, true, false)
    }


    fun showError() {
        performViewVisible(false, false, true)
    }


    fun showContent() {
        visibility = View.GONE
    }


    /**
     * 统一处理各种状态
     */
    private fun performViewVisible(isLoading: Boolean, isEmpty: Boolean, isError: Boolean) {
        when {
            isLoading -> {
                ViewUtils.setVisible(loadingContainer)
                ViewUtils.setGone(emptyContainer)
                ViewUtils.setGone(errorContainer)
            }
            isEmpty -> {
                ViewUtils.setVisible(emptyContainer)
                ViewUtils.setGone(loadingContainer)
                ViewUtils.setGone(errorContainer)
            }
            isError -> {
                ViewUtils.setVisible(errorContainer)
                ViewUtils.setGone(loadingContainer)
                ViewUtils.setGone(emptyContainer)
            }
        }
        visibility = View.VISIBLE
    }


}