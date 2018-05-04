package com.fungo.imagego.listener

/**
 * @author Pinger
 * @since 18-4-23 下午8:48
 *
 */
interface OnImageSaveListener {

    /** 图片保存成功 */
    fun onSaveSuccess(msg: String)

    /** 图片保存失败 */
    fun onSaveFail(msg: String)
}