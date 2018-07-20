package com.fungo.baselib.base.page.swipeback

import android.animation.Animator
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.fungo.baselib.R
import com.fungo.baselib.base.basic.BaseFragment

/**
 * @author Pinger
 * @since 18-7-20 下午6:29
 *
 */

open class SwipeBackFragment: BaseFragment() {

    private val SWIPEBACKFRAGMENT_STATE_SAVE_IS_HIDDEN = "SWIPEBACKFRAGMENT_STATE_SAVE_IS_HIDDEN"
    private var mSwipeBackLayout: SwipeBackLayout? = null
    private var mNoAnim: Animation? = null
    var mLocking = false

    protected var _mActivity: Activity? = null

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        _mActivity = activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            val isSupportHidden = savedInstanceState.getBoolean(SWIPEBACKFRAGMENT_STATE_SAVE_IS_HIDDEN)

            val ft = fragmentManager!!.beginTransaction()
            if (isSupportHidden) {
                ft.hide(this)
            } else {
                ft.show(this)
            }
            ft.commit()
        }

        mNoAnim = AnimationUtils.loadAnimation(activity, R.anim.anim_no)
        onFragmentCreate()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SWIPEBACKFRAGMENT_STATE_SAVE_IS_HIDDEN, isHidden)
    }

    private fun onFragmentCreate() {
        mSwipeBackLayout = SwipeBackLayout(activity)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mSwipeBackLayout!!.layoutParams = params
        mSwipeBackLayout!!.setBackgroundColor(Color.TRANSPARENT)
    }

    protected fun attachToSwipeBack(view: View): View {
        mSwipeBackLayout!!.attachToFragment(this, view)
        return mSwipeBackLayout!!
    }

    protected fun attachToSwipeBack(view: View, edgeLevel: SwipeBackLayout.EdgeLevel): View {
        mSwipeBackLayout!!.attachToFragment(this, view)
        mSwipeBackLayout!!.edgeLevel = edgeLevel
        return mSwipeBackLayout!!
    }

    protected fun setEdgeLevel(edgeLevel: SwipeBackLayout.EdgeLevel) {
        mSwipeBackLayout!!.edgeLevel = edgeLevel
    }

    protected fun setEdgeLevel(widthPixel: Int) {
        mSwipeBackLayout!!.setEdgeLevel(widthPixel)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden && mSwipeBackLayout != null) {
            mSwipeBackLayout!!.hiddenFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val view = view
        initFragmentBackground(view)
        if (view != null) {
            view.isClickable = true
        }
    }

    private fun initFragmentBackground(view: View?) {
        if (view is SwipeBackLayout) {
            val childView = view.getChildAt(0)
            setBackground(childView)
        } else {
            setBackground(view)
        }
    }

    private fun setBackground(view: View?) {
        if (view != null && view.background == null) {
            var defaultBg = 0
            if (_mActivity is SwipeBackActivity) {
                defaultBg = (_mActivity as SwipeBackActivity).getDefaultFragmentBackground()
            }
            if (defaultBg == 0) {
                val background = getWindowBackground()
                view.setBackgroundResource(background)
            } else {
                view.setBackgroundResource(defaultBg)
            }
        }
    }


    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (mLocking) {
            mNoAnim
        } else super.onCreateAnimation(transit, enter, nextAnim)
    }

    protected fun getWindowBackground(): Int {
        val a = activity!!.theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground))
        val background = a.getResourceId(0, 0)
        a.recycle()
        return background
    }

    fun getSwipeBackLayout(): SwipeBackLayout? {
        return mSwipeBackLayout
    }

    fun setSwipeBackEnable(enable: Boolean) {
        mSwipeBackLayout!!.setEnableGesture(enable)
    }


}