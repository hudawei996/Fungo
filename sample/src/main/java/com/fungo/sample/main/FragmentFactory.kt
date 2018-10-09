package com.fungo.sample.main

import com.fungo.baselib.base.page.BasePageFragment
import com.fungo.baselib.base.page.EmptyFragment
import com.fungo.sample.main.recycler.RecyclerFragment

/**
 * @author Pinger
 * @since 2018/7/26 下午10:15
 */
class FragmentFactory {

    private val mFragmentCache = HashMap<String, BasePageFragment>()

    companion object {
        @JvmStatic
        fun getInstance() = FragmentFactory()

        const val FRAGMENT_KEY = "FRAGMENT_KEY"
        const val FRAGMENT_KEY_TITLE = "FRAGMENT_KEY_TITLE"
        const val FRAGMENT_KEY_RECYCLER = "FRAGMENT_KEY_RECYCLER"
        const val FRAGMENT_KEY_BANNER = "FRAGMENT_KEY_BANNER"
    }

    fun create(key: String?): BasePageFragment {
        if (mFragmentCache.containsKey(key)) {
            val fragment = mFragmentCache[key]
            if (fragment != null) {
                return fragment
            }
        }
        return when (key) {
            FRAGMENT_KEY_RECYCLER -> RecyclerFragment()
            else -> EmptyFragment()
        }
    }
}