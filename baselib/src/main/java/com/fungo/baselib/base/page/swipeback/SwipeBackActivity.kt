package com.fungo.baselib.base.page.swipeback

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.view.ViewGroup
import com.fungo.baselib.base.basic.BaseActivity

/**
 * @author Pinger
 * @since 18-7-20 下午6:23
 * 可以右滑返回的Activity
 */

abstract class SwipeBackActivity : BaseActivity() {

    private var mSwipeBackLayout: SwipeBackLayout? = null
    private var mDefaultFragmentBackground = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onActivityCreate()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mSwipeBackLayout!!.attachToActivity(this)
    }

    private fun onActivityCreate() {
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.decorView.setBackgroundDrawable(null)
        mSwipeBackLayout = SwipeBackLayout(this)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mSwipeBackLayout!!.layoutParams = params
    }

    protected fun setEdgeLevel(edgeLevel: SwipeBackLayout.EdgeLevel) {
        mSwipeBackLayout!!.edgeLevel = edgeLevel
    }

    protected fun setEdgeLevel(widthPixel: Int) {
        mSwipeBackLayout!!.setEdgeLevel(widthPixel)
    }

    fun getSwipeBackLayout(): SwipeBackLayout? {
        return mSwipeBackLayout
    }

    fun setSwipeBackEnable(enable: Boolean) {
        mSwipeBackLayout!!.setEnableGesture(enable)
    }

    /**
     * 限制SwipeBack的条件,默认栈内Fragment数 <= 1时 , 优先滑动退出Activity , 而不是Fragment
     *
     * @return true: Activity可以滑动退出, 并且总是优先; false: Activity不允许滑动退出
     */
    fun swipeBackPriority(): Boolean {
        return supportFragmentManager.backStackEntryCount <= 1
    }

    /**
     * 当Fragment根布局 没有 设定background属性时,
     * 库默认使用Theme的android:windowbackground作为Fragment的背景,
     * 如果不像使用windowbackground作为背景, 可以通过该方法改变Fragment背景。
     */
    fun setDefaultFragmentBackground(@DrawableRes backgroundRes: Int) {
        mDefaultFragmentBackground = backgroundRes
    }

    fun getDefaultFragmentBackground(): Int {
        return mDefaultFragmentBackground
    }

}