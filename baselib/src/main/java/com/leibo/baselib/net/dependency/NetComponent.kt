package com.leibo.baselib.net.dependency

import com.leibo.baselib.app.AppModule
import com.leibo.baselib.net.retrofit.FungoRequest
import dagger.Component
import javax.inject.Singleton

/**
 * @author Pinger
 * @since 2018/4/1 16:50
 */
@Singleton
@Component(modules = [(NetModule::class), (AppModule::class)])
interface NetComponent {
    fun getFungoRequest(): FungoRequest
}