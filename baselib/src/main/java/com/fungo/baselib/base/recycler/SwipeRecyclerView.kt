package com.fungo.baselib.base.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.fungo.baselib.R

/**
 * @author Pinger
 * @since 18-7-25 下午3:23
 * 将滑动布局包裹一层，方便随时替换
 */

class SwipeRecyclerView : FrameLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr, 0)

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_swipe_recycler, this)
    }






}