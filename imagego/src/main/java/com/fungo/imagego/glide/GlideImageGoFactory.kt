package com.fungo.imagego.glide

import com.fungo.imagego.create.ImageGoFactory
import com.fungo.imagego.create.ImageGoStrategy


/**
 * @author Pinger
 * @since 18-5-4 下午7:46
 */

class GlideImageGoFactory : ImageGoFactory() {

    override fun create(): ImageGoStrategy {
        return GlideImageGoStrategy()
    }
}