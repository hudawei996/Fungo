package com.fungo.imagego.create

/**
 * @author Pinger
 * @since 2017/5/21 0021 下午 6:58
 * 工厂接口创建底层图片加载对象
 */
abstract class ImageGoFactory {

    /**
     * 创建抽象工厂，图片加载策略
     */
    abstract fun create(): ImageGoStrategy
}
