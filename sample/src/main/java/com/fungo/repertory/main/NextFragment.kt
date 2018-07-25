package com.fungo.repertory.main

import com.fungo.baselib.base.page.BasePageFragment
import com.fungo.repertory.R

/**
 * @author Pinger
 * @since 18-7-24 下午6:01
 *
 */

class NextFragment : BasePageFragment() {


    override fun getContentResId(): Int {
        return R.layout.fragment_next
    }


    override fun getPageTitle(): String {
        return "我就是下一个Fragment"
    }
}