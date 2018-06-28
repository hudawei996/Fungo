package com.fungo.imagego.picasso

import com.fungo.imagego.create.ImageGoFactory
import com.fungo.imagego.create.ImageGoStrategy

/**
 * @author Pinger
 * @since 18-6-12 下午2:41
 *
 */

class PicassoImageGoFactory : ImageGoFactory() {

    override fun create(): ImageGoStrategy {
        return PicassoImageGoStrategy()
    }
}