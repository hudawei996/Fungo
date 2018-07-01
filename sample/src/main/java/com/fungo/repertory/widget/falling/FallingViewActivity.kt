package com.fungo.repertory.widget.falling

import android.support.v4.content.ContextCompat
import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.baseuilib.view.falling.FallingEntity
import com.fungo.baseuilib.view.falling.FallingView
import com.fungo.repertory.R

/**
 * @author Pinger
 * @since 2018/1/11 0011 上午 11:51
 */

class FallingViewActivity : BaseActivity() {
    override val layoutResID: Int
        get() = R.layout.activity_falling_view

    override fun initView() {
        setToolBar(getString(R.string.widget_view_falling_snow), true)

        val fallingView = findViewById<FallingView>(R.id.falling_view)

        val builder = FallingEntity().Builder(ContextCompat.getDrawable(this, R.mipmap.ic_snow)!!)
        val fallingEntity = builder
                .setSpeed(7, true)
                .setSize(50, 50, true)
                .setWind(5, true, true)
                .build()

        fallingView.addFallEntity(fallingEntity, 100)
    }
}
