package com.fungo.repertory.app

import com.fungo.baselib.app.BaseApplication
import com.fungo.imagego.ImageManager
import com.fungo.imagego.glide.GlideImageGoFactory
import com.fungo.repertory.constant.AppConstant
import com.fungo.socialgo.share.config.PlatformConfig

/**
 * @author Pinger
 * @since 3/29/18 9:13 PM
 */
class SampleApplication : BaseApplication() {

    override fun initSDK() {
        ImageManager.instance.setImageGoFactory(GlideImageGoFactory())
        PlatformConfig.setQQ(AppConstant.QQ_APP_ID)
    }

}