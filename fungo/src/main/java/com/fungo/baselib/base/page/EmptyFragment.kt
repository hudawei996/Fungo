package com.fungo.baselib.base.page

import com.fungo.baselib.R

/**
 * @author Pinger
 * @since 2018/7/26 下午10:19
 */
class EmptyFragment : BasePageFragment() {


    override fun getPageTitle(): String? {
        return "暂无数据"
    }

    override fun getContentResId(): Int {
        return R.layout.base_fragment_empty
    }
}