package com.leibo.baselib.image.listener

/**
 * @author Pinger
 * @since 18-4-23 下午8:48
 *
 */
interface ImageSaveListener {

    fun onSaveSuccess(msg: String)

    fun onSaveFail(msg: String)

}