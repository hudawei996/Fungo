package com.fungo.baselib.app

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Pinger
 * @since 2018/4/1 16:47
 */
@Module
class AppModule(val context: Context) {

    @Singleton
    @Provides
    internal fun provideContext(): Context {
        return context
    }
}