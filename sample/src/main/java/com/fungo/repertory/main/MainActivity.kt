package com.fungo.repertory.main

import android.view.View
import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.repertory.R
import com.fungo.repertory.image.ImageMainActivity
import com.fungo.repertory.player.PlayerMainActivity
import com.fungo.repertory.recycler.RecyclerPageActivity
import com.fungo.repertory.widget.CustomViewActivity

class MainActivity : BaseActivity() {

    override val layoutResID: Int
        get() = R.layout.activity_main

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_widget -> startActivity(CustomViewActivity::class.java)
            R.id.btn_recycler -> startActivity(RecyclerPageActivity::class.java)
            R.id.btn_player -> startActivity(PlayerMainActivity::class.java)
            R.id.btn_image -> startActivity(ImageMainActivity::class.java)
        }
    }

}
