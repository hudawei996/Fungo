package com.fungo.repertory.recycler

import android.widget.Toast
import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.baselib.net.repository.Repository
import com.fungo.repertory.R
import kotlinx.android.synthetic.main.activity_recycler_view.*
import java.util.function.Consumer

/**
 * @author Pinger
 * @since 2018/1/13 0013 上午 11:11
 */

class RecyclerViewActivity : BaseActivity() {

    override val layoutResID: Int
        get() = R.layout.activity_recycler_view

    override fun initView() {
        setActionBar(getString(R.string.recycler_view), true)

        textView.setOnClickListener({
            Toast.makeText(this@RecyclerViewActivity,"哈哈哈哈",Toast.LENGTH_SHORT).show()
        })
    }

    override fun initData() {
        val url = ""




    }
}
