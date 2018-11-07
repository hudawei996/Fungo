package com.fungo.sample.test

import android.os.Bundle
import com.fungo.baseuilib.activity.BaseActivity
import com.fungo.baseuilib.activity.BaseContentActivity
import com.fungo.baseuilib.fragment.BaseFragment
import com.fungo.baseuilib.fragment.PlaceholderFragment
import com.fungo.baseuilib.utils.StatusBarUtils
import com.fungo.sample.R

/**
 * @author Pinger
 * @since 2018/11/3 20:03
 */
class TestActivity(override val layoutResID: Int = R.layout.fragment_imagego) : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.clearFullScreen(this)
    }

    override fun isStatusBarTranslate(): Boolean =false


    override fun isStatusBarForegroundBlack(): Boolean {
        return false
    }

}