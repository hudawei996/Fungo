package com.fungo.repertory.main

import com.fungo.baselib.base.page.BasePageActivity
import com.fungo.baselib.base.page.BasePageFragment
import com.fungo.repertory.R

/**
 * @author Pinger
 * @since 18-7-24 上午11:54
 * 根据不同的id创建不同的Fragment
 */

class PageActivity : BasePageActivity() {

    override fun getRootFragment(): BasePageFragment {
        return MainFragment()
    }


}