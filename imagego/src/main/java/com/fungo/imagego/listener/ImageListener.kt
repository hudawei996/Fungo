package com.fungo.imagego.listener

/**
 * @author Pinger
 * @since 3/28/18 2:22 PM
 */
interface ImageListener {

    fun onSuccess()

    fun onFail(msg: String)
}