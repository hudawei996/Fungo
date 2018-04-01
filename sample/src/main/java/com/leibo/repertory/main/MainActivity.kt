package com.leibo.repertory.main

import android.view.View
import com.leibo.baselib.base.basic.BaseActivity
import com.leibo.repertory.R
import com.leibo.repertory.image.ImageTestActivity
import com.leibo.repertory.player.PlayerMainActivity
import com.leibo.repertory.recycler.RecyclerPageActivity
import com.leibo.repertory.widget.CustomViewActivity

class MainActivity : BaseActivity() {

    override val layoutResID: Int
        get() = R.layout.activity_main

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_widget -> startActivity(CustomViewActivity::class.java)
            R.id.btn_recycler -> startActivity(RecyclerPageActivity::class.java)
            R.id.btn_player -> startActivity(PlayerMainActivity::class.java)
            R.id.btn_image -> startActivity(ImageTestActivity::class.java)
        }
    }

}
