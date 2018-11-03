package com.fungo.sample.test

import com.fungo.baseuilib.activity.BaseContentActivity
import com.fungo.baseuilib.fragment.BaseFragment
import com.fungo.baseuilib.fragment.PlaceholderFragment

/**
 * @author Pinger
 * @since 2018/11/3 20:03
 */
class TestActivity : BaseContentActivity() {

    override fun getContentFragment(): BaseFragment {
        return PlaceholderFragment.newInstance(isShowToolbar = false)
    }
}