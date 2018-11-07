package com.fungo.sample.main

import com.fungo.baseuilib.activity.BaseContentActivity
import com.fungo.baseuilib.fragment.BaseFragment

/**
 * @author Pinger
 * @since 18-7-24 下午2:56
 * 填充一个主页
 */

class MainActivity : BaseContentActivity() {


    override fun getContentFragment(): BaseFragment {
        return MainFragment()
    }
}
