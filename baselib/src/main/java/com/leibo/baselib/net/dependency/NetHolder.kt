package com.leibo.baselib.net.dependency

/**
 * @author Pinger
 * @since 2018/4/1 17:08
 */
class NetHolder {

    companion object {
        private var mNetComponent: NetComponent? = null
        fun setNetComponent(component: NetComponent) {
            this.mNetComponent = component
        }

        fun getNetComponent(): NetComponent {
            return mNetComponent!!
        }
    }
}