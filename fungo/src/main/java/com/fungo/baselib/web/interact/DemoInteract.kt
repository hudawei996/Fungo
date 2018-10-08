package com.fungo.baselib.web.interact

import android.content.Context
import android.content.Intent
import android.webkit.WebView
import com.fungo.baselib.web.sonic.SonicSessionClientImpl


/**
 * @author Pinger
 * @since 18-7-4 下午1:20
 * Demo
 */

class DemoInteract(sessionClient: SonicSessionClientImpl?, intent: Intent?, context: Context?, webView: WebView?) : MainInteract(sessionClient, intent, context, webView) {

    override val name: String
        get() = "Deme"
}
