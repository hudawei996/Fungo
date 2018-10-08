package com.fungo.baselib.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.AnimRes
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.view.View
import java.lang.reflect.InvocationTargetException

/**
 * @author Pinger
 * @since 3/26/18 9:07 PM
 */
object ActivityUtils {

    /**
     * Return whether the activity exists.
     *
     * @param pkg The name of the package.
     * @param cls The name of the class.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isActivityExists(pkg: String,
                         cls: String): Boolean {
        val intent = Intent()
        intent.setClassName(pkg, cls)
        return !(BaseUtils.getApp().packageManager.resolveActivity(intent, 0) == null ||
                intent.resolveActivity(BaseUtils.getApp().packageManager) == null ||
                BaseUtils.getApp().packageManager.queryIntentActivities(intent, 0).size == 0)
    }

    /**
     * Start the activity.
     *
     * @param intent The description of the activity to start.
     */
    fun startActivity(intent: Intent) {
        startActivity(intent, getActivityOrApp(), null)
    }

    /**
     * Start the activity.
     *
     * @param intent  The description of the activity to start.
     * @param options Additional options for how the Activity should be started.
     */
    fun startActivity(intent: Intent,
                      options: Bundle) {
        startActivity(intent, getActivityOrApp(), options)
    }

    /**
     * Start the activity.
     *
     * @param intent    The description of the activity to start.
     * @param enterAnim A resource ID of the animation resource to use for the
     * incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     * outgoing activity.
     */
    fun startActivity(intent: Intent,
                      @AnimRes enterAnim: Int,
                      @AnimRes exitAnim: Int) {
        val context = getActivityOrApp()
        startActivity(intent, context, getOptionsBundle(context, enterAnim, exitAnim))
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN && context is Activity) {
            (context as Activity).overridePendingTransition(enterAnim, exitAnim)
        }
    }

    /**
     * Start the activity.
     *
     * @param activity The activity.
     * @param intent   The description of the activity to start.
     */
    fun startActivity(activity: Activity,
                      intent: Intent) {
        startActivity(intent, activity, null)
    }

    /**
     * Start the activity.
     *
     * @param activity The activity.
     * @param intent   The description of the activity to start.
     * @param options  Additional options for how the Activity should be started.
     */
    fun startActivity(activity: Activity,
                      intent: Intent,
                      options: Bundle?) {
        startActivity(intent, activity, options)
    }

    /**
     * Start the activity.
     *
     * @param activity       The activity.
     * @param intent         The description of the activity to start.
     * @param sharedElements The names of the shared elements to transfer to the called
     * Activity and their associated Views.
     */
    fun startActivity(activity: Activity,
                      intent: Intent,
                      sharedElements: Array<View>) {
        startActivity(intent, activity, getOptionsBundle(activity, sharedElements))
    }

    /**
     * Start the activity.
     *
     * @param activity  The activity.
     * @param intent    The description of the activity to start.
     * @param enterAnim A resource ID of the animation resource to use for the
     * incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     * outgoing activity.
     */
    fun startActivity(activity: Activity,
                      intent: Intent,
                      @AnimRes enterAnim: Int,
                      @AnimRes exitAnim: Int) {
        startActivity(intent, activity, getOptionsBundle(activity, enterAnim, exitAnim))
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim)
        }
    }

    /**
     * Start activities.
     *
     * @param intents The descriptions of the activities to start.
     */
    fun startActivities(intents: Array<Intent>) {
        startActivities(intents, getActivityOrApp(), null)
    }

    /**
     * Start activities.
     *
     * @param intents The descriptions of the activities to start.
     * @param options Additional options for how the Activity should be started.
     */
    fun startActivities(intents: Array<Intent>,
                        options: Bundle?) {
        startActivities(intents, getActivityOrApp(), options)
    }

    /**
     * Start activities.
     *
     * @param intents   The descriptions of the activities to start.
     * @param enterAnim A resource ID of the animation resource to use for the
     * incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     * outgoing activity.
     */
    fun startActivities(intents: Array<Intent>,
                        @AnimRes enterAnim: Int,
                        @AnimRes exitAnim: Int) {
        val context = getActivityOrApp()
        startActivities(intents, context, getOptionsBundle(context, enterAnim, exitAnim))
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN && context is Activity) {
            context.overridePendingTransition(enterAnim, exitAnim)
        }
    }

    /**
     * Start activities.
     *
     * @param activity The activity.
     * @param intents  The descriptions of the activities to start.
     */
    fun startActivities(activity: Activity,
                        intents: Array<Intent>) {
        startActivities(intents, activity, null)
    }

    /**
     * Start activities.
     *
     * @param activity The activity.
     * @param intents  The descriptions of the activities to start.
     * @param options  Additional options for how the Activity should be started.
     */
    fun startActivities(activity: Activity,
                        intents: Array<Intent>,
                        options: Bundle?) {
        startActivities(intents, activity, options)
    }

    /**
     * Start activities.
     *
     * @param activity  The activity.
     * @param intents   The descriptions of the activities to start.
     * @param enterAnim A resource ID of the animation resource to use for the
     * incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     * outgoing activity.
     */
    fun startActivities(activity: Activity,
                        intents: Array<Intent>,
                        @AnimRes enterAnim: Int,
                        @AnimRes exitAnim: Int) {
        startActivities(intents, activity, getOptionsBundle(activity, enterAnim, exitAnim))
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim)
        }
    }

    /**
     * Start home activity.
     */
    fun startHomeActivity() {
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        startActivity(homeIntent)
    }

    /**
     * Return the list of activity.
     *
     * @return the list of activity
     */
    fun getActivityList(): List<Activity> {
        return BaseUtils.getActivityList()
    }

    /**
     * Return the name of launcher activity.
     *
     * @return the name of launcher activity
     */
    fun getLauncherActivity(): String {
        return getLauncherActivity(BaseUtils.getApp().packageName)
    }

    /**
     * Return the name of launcher activity.
     *
     * @param pkg The name of the package.
     * @return the name of launcher activity
     */
    fun getLauncherActivity(pkg: String): String {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pm = BaseUtils.getApp().packageManager
        val info = pm.queryIntentActivities(intent, 0)
        for (aInfo in info) {
            if (aInfo.activityInfo.packageName == pkg) {
                return aInfo.activityInfo.name
            }
        }
        return "no $pkg"
    }

    /**
     * Return the top activity in activity's stack.
     *
     * @return the top activity in activity's stack
     */
    @SuppressLint("PrivateApi")
    fun getTopActivity(): Activity? {
        val topActivity = BaseUtils.getActivityList().last
        if (topActivity != null) {
            return topActivity
        }
        // using reflect to get top activity
        try {
            @SuppressLint("PrivateApi")
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null)
            val activitiesField = activityThreadClass.getDeclaredField("mActivities")
            activitiesField.isAccessible = true
            val activities = activitiesField.get(activityThread) as Map<*, Object>
            for (activityRecord in activities.values) {
                val activityRecordClass = activityRecord.javaClass
                val pausedField = activityRecordClass.getDeclaredField("paused")
                pausedField.isAccessible = true
                if (!pausedField.getBoolean(activityRecord)) {
                    val activityField = activityRecordClass.getDeclaredField("activity")
                    activityField.isAccessible = true
                    val activity = activityField.get(activityRecord) as Activity
                    BaseUtils.setTopActivity(activity)
                    return activity
                }
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

        return null
    }

    private fun getActivityOrApp(): Context {
        val topActivity = getTopActivity()
        return topActivity ?: BaseUtils.getApp()
    }

    private fun startActivity(context: Context,
                              extras: Bundle?,
                              pkg: String,
                              cls: String,
                              options: Bundle?) {
        val intent = Intent(Intent.ACTION_VIEW)
        if (extras != null) intent.putExtras(extras)
        intent.component = ComponentName(pkg, cls)
        startActivity(intent, context, options)
    }

    private fun startActivity(intent: Intent,
                              context: Context,
                              options: Bundle?) {
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (options != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            context.startActivity(intent, options)
        } else {
            context.startActivity(intent)
        }
    }

    private fun startActivities(intents: Array<Intent>,
                                context: Context,
                                options: Bundle?) {
        if (context !is Activity) {
            for (intent in intents) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
        if (options != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            context.startActivities(intents, options)
        } else {
            context.startActivities(intents)
        }
    }

    private fun getOptionsBundle(context: Context,
                                 enterAnim: Int,
                                 exitAnim: Int): Bundle? {
        return ActivityOptionsCompat.makeCustomAnimation(context, enterAnim, exitAnim).toBundle()
    }

    private fun getOptionsBundle(activity: Activity,
                                 sharedElements: Array<View>): Bundle? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val len = sharedElements.size
            val pairs = arrayOfNulls<Pair<View, String>>(len)
            for (i in 0 until len) {
                pairs[i] = Pair.create(sharedElements[i], sharedElements[i].transitionName)
            }
            return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pairs).toBundle()
        }
        return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, null, null).toBundle()
    }

}