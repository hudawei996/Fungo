package com.fungo.repertory.main

import android.view.View
import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.repertory.R
import com.fungo.repertory.image.ImageMainActivity
import com.fungo.repertory.player.PlayerMainActivity
import com.fungo.repertory.recycler.RecyclerPageActivity
import com.fungo.repertory.social.SocialActivity
import com.fungo.repertory.widget.CustomViewActivity

class MainActivity : BaseActivity() {

    override val layoutResID: Int
        get() = R.layout.activity_main

    fun onWidget(view: View) {
        startActivity(CustomViewActivity::class.java)
    }

    fun onRecycler(view: View) {
        startActivity(RecyclerPageActivity::class.java)
    }

    fun onVideoPlayer(view: View) {
        startActivity(PlayerMainActivity::class.java)
    }

    fun onImageManager(view: View) {
        startActivity(ImageMainActivity::class.java)
    }

    fun onSocial(view: View) {
        startActivity(SocialActivity::class.java)
    }
}
