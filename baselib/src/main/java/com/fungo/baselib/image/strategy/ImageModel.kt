package com.fungo.baselib.image.strategy

/**
 * @author Pinger
 * @since 3/28/18 5:10 PM
 *
 * 图片加载模式，默认提供三种模式，这里只实现Glide加载
 */
object ImageModel {

    const val MODEL_GLIDE = 0     // Glide
    const val MODEL_PICASSO = 1   // Picasso
    const val MODEL_FRESCO = 2    // Fresco
}