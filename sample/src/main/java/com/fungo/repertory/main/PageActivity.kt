package com.fungo.repertory.main

import android.content.Context
import android.content.Intent
import com.fungo.baselib.base.page.BasePageActivity
import com.fungo.baselib.base.page.BasePageFragment

/**
 * @author Pinger
 * @since 18-7-24 上午11:54
 * 根据不同的id创建不同的Fragment
 */

class PageActivity : BasePageActivity() {

    override fun getRootFragment(): BasePageFragment {
        return FragmentFactory.getInstance().create(intent.getStringExtra(FragmentFactory.FRAGMENT_KEY))
    }

    companion object {
        fun start(context: Context, key: String, title: String) {
            val intent = Intent(context, PageActivity::class.java)
            intent.putExtra(FragmentFactory.FRAGMENT_KEY, key)
            intent.putExtra(FragmentFactory.FRAGMENT_KEY_TITLE, title)
            context.startActivity(intent)
        }
    }
}