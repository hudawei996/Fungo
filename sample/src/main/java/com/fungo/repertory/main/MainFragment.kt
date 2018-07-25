package com.fungo.repertory.main

import android.view.View
import com.fungo.baselib.base.page.BasePageFragment
import com.fungo.baselib.web.WebActivity
import com.fungo.repertory.R
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * @author Pinger
 * @since 18-7-24 下午1:09
 *
 */

class MainFragment : BasePageFragment() {


    override fun getContentResId(): Int {
        return R.layout.fragment_main
    }


    override fun getPageTitle(): String {
        return "我就是主页了"
    }


    override fun initView() {
        button.setOnClickListener {
            WebActivity.start(context!!, "https://baidu.com","百度一下")
        }
    }

}