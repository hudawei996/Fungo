package com.fungo.baselib.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.util.*

/**
 * @author Pinger
 * @since 3/26/18 9:21 PM
 */

@SuppressLint("StaticFieldLeak")
object PermissionUtils {

    private val PERMISSIONS = getPermissions()

    private lateinit var sInstance: PermissionUtils

    private lateinit var mPermissions: MutableSet<String>
    private lateinit var mPermissionsRequest: MutableList<String>
    private lateinit var mPermissionsGranted: MutableList<String>
    private lateinit var mPermissionsDenied: MutableList<String>
    private lateinit var mPermissionsDeniedForever: MutableList<String>

    private var mSimpleCallback: SimpleCallback? = null
    private var mFullCallback: FullCallback? = null
    private var mThemeCallback: ThemeCallback? = null
    private var mOnRationaleListener: OnRationaleListener? = null


    /**
     * Return the permissions used in application.
     *
     * @return the permissions used in application
     */
    fun getPermissions(): List<String> {
        return getPermissions(AppUtils.getContext().packageName)
    }

    /**
     * Return the permissions used in application.
     *
     * @param packageName The name of the package.
     * @return the permissions used in application
     */
    fun getPermissions(packageName: String): List<String> {
        val pm = AppUtils.getContext().packageManager
        return try {
            Arrays.asList(
                    *pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
                            .requestedPermissions
            )
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            emptyList()
        }

    }

    /**
     * Return whether *you* have granted the permissions.
     *
     * @param permissions The permissions.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isGranted(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (!isGranted(permission)) {
                return false
            }
        }
        return true
    }

    private fun isGranted(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(AppUtils.getContext(),
                permission)
    }

    /**
     * Launch the application's details settings.
     */
    fun launchAppDetailsSettings() {
        val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
        intent.data = Uri.parse("package:" + AppUtils.getContext().packageName)
        AppUtils.getContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    /**
     * Set the permissions.
     *
     * @param permissions The permissions.
     * @return the single [PermissionUtils] instance
     */
    fun permission(@PermissionConstants.Permission permissions: Array<String>): PermissionUtils {
        return initPermission(permissions)
    }

    private fun initPermission(permissions: Array<String>): PermissionUtils {
        mPermissions = LinkedHashSet()
        for (permission in permissions) {
            for (aPermission in PermissionConstants.getPermissions(permission)) {
                if (PERMISSIONS.contains(aPermission)) {
                    mPermissions.add(aPermission)
                }
            }
        }
        sInstance = this
        return sInstance
    }

    /**
     * Set rationale listener.
     *
     * @param listener The rationale listener.
     * @return the single [PermissionUtils] instance
     */
    fun rationale(listener: OnRationaleListener): PermissionUtils {
        mOnRationaleListener = listener
        return this
    }

    /**
     * Set the simple call back.
     *
     * @param callback the simple call back
     * @return the single [PermissionUtils] instance
     */
    fun callback(callback: SimpleCallback): PermissionUtils {
        mSimpleCallback = callback
        return this
    }

    /**
     * Set the full call back.
     *
     * @param callback the full call back
     * @return the single [PermissionUtils] instance
     */
    fun callback(callback: FullCallback): PermissionUtils {
        mFullCallback = callback
        return this
    }

    /**
     * Set the theme callback.
     *
     * @param callback The theme callback.
     * @return the single [PermissionUtils] instance
     */
    fun theme(callback: ThemeCallback): PermissionUtils {
        mThemeCallback = callback
        return this
    }

    /**
     * Start request.
     */
    fun request() {
        mPermissionsGranted = ArrayList()
        mPermissionsRequest = ArrayList()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mPermissionsGranted.addAll(mPermissions)
            requestCallback()
        } else {
            for (permission in mPermissions) {
                if (isGranted(permission)) {
                    mPermissionsGranted.add(permission)
                } else {
                    mPermissionsRequest.add(permission)
                }
            }
            if (mPermissionsRequest.isEmpty()) {
                requestCallback()
            } else {
                startPermissionActivity()
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun startPermissionActivity() {
        mPermissionsDenied = ArrayList()
        mPermissionsDeniedForever = ArrayList()
        PermissionActivity.start(AppUtils.getContext())
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun rationale(activity: Activity): Boolean {
        var isRationale = false
        if (mOnRationaleListener != null) {
            for (permission in mPermissionsRequest) {
                if (activity.shouldShowRequestPermissionRationale(permission)) {
                    getPermissionsStatus(activity)
                    mOnRationaleListener!!.rationale(object : OnRationaleListener.ShouldRequest {
                        override fun again(again: Boolean) {
                            if (again) {
                                startPermissionActivity()
                            } else {
                                requestCallback()
                            }
                        }
                    })
                    isRationale = true
                    break
                }
            }
            mOnRationaleListener = null
        }
        return isRationale
    }

    private fun getPermissionsStatus(activity: Activity) {
        for (permission in mPermissionsRequest) {
            if (isGranted(permission)) {
                mPermissionsGranted.add(permission)
            } else {
                mPermissionsDenied.add(permission)
                if (!activity.shouldShowRequestPermissionRationale(permission)) {
                    mPermissionsDeniedForever.add(permission)
                }
            }
        }
    }

    private fun requestCallback() {
        if (mSimpleCallback != null) {
            if (mPermissionsRequest.size == 0 || mPermissions.size == mPermissionsGranted.size) {
                mSimpleCallback!!.onGranted()
            } else {
                if (!mPermissionsDenied.isEmpty()) {
                    mSimpleCallback!!.onDenied()
                }
            }
            mSimpleCallback = null
        }
        if (mFullCallback != null) {
            if (mPermissionsRequest.size == 0 || mPermissions.size == mPermissionsGranted.size) {
                mFullCallback!!.onGranted(mPermissionsGranted)
            } else {
                if (!mPermissionsDenied.isEmpty()) {
                    mFullCallback!!.onDenied(mPermissionsDeniedForever, mPermissionsDenied)
                }
            }
            mFullCallback = null
        }
        mOnRationaleListener = null
        mThemeCallback = null
    }

    private fun onRequestPermissionsResult(activity: Activity) {
        getPermissionsStatus(activity)
        requestCallback()
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    class PermissionActivity : Activity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            if (sInstance.mThemeCallback != null) {
                sInstance.mThemeCallback!!.onActivityCreate(this)
            }
            super.onCreate(savedInstanceState)

            if (sInstance.rationale(this)) {
                finish()
                return
            }
            if (sInstance.mPermissionsRequest != null) {
                val size = sInstance.mPermissionsRequest.size
                if (size <= 0) {
                    finish()
                    return
                }
                requestPermissions(sInstance.mPermissionsRequest.toTypedArray(), 1)
            }
        }

        override fun onRequestPermissionsResult(requestCode: Int,
                                                permissions: Array<String>,
                                                grantResults: IntArray) {
            sInstance.onRequestPermissionsResult(this)
            finish()
        }

        companion object {

            fun start(context: Context) {
                val starter = Intent(context, PermissionActivity::class.java)
                starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(starter)
            }
        }
    }


    interface OnRationaleListener {

        fun rationale(shouldRequest: ShouldRequest)

        interface ShouldRequest {
            fun again(again: Boolean)
        }
    }

    interface SimpleCallback {
        fun onGranted()

        fun onDenied()
    }

    interface FullCallback {
        fun onGranted(permissionsGranted: List<String>)

        fun onDenied(permissionsDeniedForever: List<String>?, permissionsDenied: List<String>)
    }

    interface ThemeCallback {
        fun onActivityCreate(activity: Activity)
    }


}