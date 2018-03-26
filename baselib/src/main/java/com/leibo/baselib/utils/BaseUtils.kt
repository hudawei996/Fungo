package com.leibo.baselib.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import java.util.*

/**
 * @author Pinger
 * @since 3/26/18 9:00 PM
 * 工具类初始化
 */
@SuppressLint("StaticFieldLeak")
object BaseUtils {

    private lateinit var mApplication: Application
    private val mActivityList = LinkedList<Activity>()

    /**
     * 初始化
     */
    fun init(context: Context) {
        mApplication = context.applicationContext as Application
        mApplication.registerActivityLifecycleCallbacks(mCallbacks)

    }

    /**
     * 获取Application
     */
    fun getApp(): Application {
        return mApplication
    }

    /**
     * 设置栈顶Activity
     */
    fun setTopActivity(activity: Activity) {
        if (activity.javaClass == PermissionUtils.PermissionActivity::class.java) return
        if (mActivityList.contains(activity)) {
            if (mActivityList.last != activity) {
                mActivityList.remove(activity)
                mActivityList.addLast(activity)
            }
        } else {
            mActivityList.addLast(activity)
        }
    }

    /**
     * 获取所有启动过的Activity
     */
    fun getActivityList(): LinkedList<Activity> {
        return mActivityList
    }

    /**
     * Activity生命周期回调
     */
    private val mCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, bundle: Bundle) {
            setTopActivity(activity)
        }

        override fun onActivityStarted(activity: Activity) {
            setTopActivity(activity)
        }

        override fun onActivityResumed(activity: Activity) {
            setTopActivity(activity)
        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            mActivityList.remove(activity)
        }
    }


}