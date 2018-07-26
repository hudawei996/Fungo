package com.fungo.baselib.web

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.fungo.baselib.base.page.BasePageActivity
import com.fungo.baselib.base.page.BasePageFragment
import com.fungo.baselib.constant.IntentConstant

/**
 * @author Pinger
 * @since 18-7-25 下午2:18
 * 跳转Web页面的中转，只用来分发H5Fragment页面
 */

class WebActivity : BasePageActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
    }

    override fun getRootFragment(): BasePageFragment {
        return WebFragment()
    }


    /**
     * 跳转暂时使用传统方法
     */
    companion object {
        fun start(context: Context, url: String, title: String, canBack: Boolean) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(IntentConstant.KEY_WEB_URL, url)
            intent.putExtra(IntentConstant.KEY_WEB_TITLE, title)
            intent.putExtra(IntentConstant.KEY_WEB_BACK, canBack)
            context.startActivity(intent)
        }

        fun start(context: Context, url: String, canBack: Boolean) {
            start(context, url, "", canBack)
        }

        fun start(context: Context, url: String, title: String) {
            start(context, url, title, true)
        }

        fun start(context: Context, url: String) {
            start(context, url, "", true)
        }
    }

}