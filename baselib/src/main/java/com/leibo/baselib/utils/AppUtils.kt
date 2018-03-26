package com.leibo.baselib.utils

import android.app.Activity
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import java.io.File
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * @author Pinger
 * @since 3/26/18 10:03 PM
 */
object AppUtils {


    /**
     * Install the app.
     *
     * Target APIs greater than 25 must hold
     * `<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />`
     *
     * @param filePath  The path of file.
     * @param authority Target APIs greater than 23 must hold the authority of a FileProvider
     * defined in a `<provider>` element in your app's manifest.
     */
    fun installApp(filePath: String, authority: String) {
        installApp(getFileByPath(filePath), authority)
    }

    /**
     * Install the app.
     *
     * Target APIs greater than 25 must hold
     * `<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />`
     *
     * @param file      The file.
     * @param authority Target APIs greater than 23 must hold the authority of a FileProvider
     * defined in a `<provider>` element in your app's manifest.
     */
    fun installApp(file: File?, authority: String) {
        if (!isFileExists(file)) return
        BaseUtils.getApp().startActivity(IntentUtils.getInstallAppIntent(file, authority, true))
    }


    /**
     * Install the app.
     *
     * Target APIs greater than 25 must hold
     * `<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />`
     *
     * @param activity    The activity.
     * @param filePath    The path of file.
     * @param authority   Target APIs greater than 23 must hold the authority of a FileProvider
     * defined in a `<provider>` element in your app's manifest.
     * @param requestCode If &gt;= 0, this code will be returned in
     * onActivityResult() when the activity exits.
     */
    fun installApp(activity: Activity,
                   filePath: String,
                   authority: String,
                   requestCode: Int) {
        installApp(activity, getFileByPath(filePath), authority, requestCode)
    }

    /**
     * Install the app.
     *
     * Target APIs greater than 25 must hold
     * `<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />`
     *
     * @param activity    The activity.
     * @param file        The file.
     * @param authority   Target APIs greater than 23 must hold the authority of a FileProvider
     * defined in a `<provider>` element in your app's manifest.
     * @param requestCode If &gt;= 0, this code will be returned in
     * onActivityResult() when the activity exits.
     */
    fun installApp(activity: Activity,
                   file: File?,
                   authority: String,
                   requestCode: Int) {
        if (!isFileExists(file)) return
        activity.startActivityForResult(IntentUtils.getInstallAppIntent(file!!, authority),
                requestCode)
    }

    /**
     * Install the app silently.
     *
     * Without root permission must hold
     * `<uses-permission android:name="android.permission.INSTALL_PACKAGES" />`
     *
     * @param filePath The path of file.
     * @return `true`: success<br></br>`false`: fail
     */
    fun installAppSilent(filePath: String): Boolean {
        return installAppSilent(getFileByPath(filePath), null)
    }

    /**
     * Install the app silently.
     *
     * Without root permission must hold
     * `<uses-permission android:name="android.permission.INSTALL_PACKAGES" />`
     *
     * @param file The file.
     * @return `true`: success<br></br>`false`: fail
     */
    fun installAppSilent(file: File): Boolean {
        return installAppSilent(file, null)
    }


    /**
     * Install the app silently.
     *
     * Without root permission must hold
     * `<uses-permission android:name="android.permission.INSTALL_PACKAGES" />`
     *
     * @param filePath The path of file.
     * @param params   The params of installation.
     * @return `true`: success<br></br>`false`: fail
     */
    fun installAppSilent(filePath: String, params: String): Boolean {
        return installAppSilent(getFileByPath(filePath), null)
    }

    /**
     * Install the app silently.
     *
     * Without root permission must hold
     * `<uses-permission android:name="android.permission.INSTALL_PACKAGES" />`
     *
     * @param file   The file.
     * @param params The params of installation.
     * @return `true`: success<br></br>`false`: fail
     */
    fun installAppSilent(file: File?, params: String?): Boolean {
        if (!isFileExists(file)) return false
        val isRoot = isDeviceRooted()
        val filePath = file!!.absolutePath
        val command = ("LD_LIBRARY_PATH=/vendor/lib*:/system/lib* pm install " +
                (if (params == null) "" else "$params ")
                + filePath)
        val commandResult = ShellUtils.execCmd(command, isRoot)
        return if (commandResult.successMsg != null && commandResult.successMsg?.toLowerCase()!!.contains("success")) {
            true
        } else {
            Log.e("AppUtils", "installAppSilent successMsg: " + commandResult.successMsg +
                    ", errorMsg: " + commandResult.errorMsg)
            false
        }
    }

    /**
     * Uninstall the app.
     *
     * @param packageName The name of the package.
     */
    fun uninstallApp(packageName: String) {
        if (isSpace(packageName)) return
        BaseUtils.getApp().startActivity(IntentUtils.getUninstallAppIntent(packageName, true))
    }

    /**
     * Uninstall the app.
     *
     * @param activity    The activity.
     * @param packageName The name of the package.
     * @param requestCode If &gt;= 0, this code will be returned in
     * onActivityResult() when the activity exits.
     */
    fun uninstallApp(activity: Activity,
                     packageName: String,
                     requestCode: Int) {
        if (isSpace(packageName)) return
        activity.startActivityForResult(
                IntentUtils.getUninstallAppIntent(packageName),
                requestCode
        )
    }

    /**
     * Uninstall the app silently.
     *
     * Without root permission must hold
     * `<uses-permission android:name="android.permission.DELETE_PACKAGES" />`
     *
     * @param packageName The name of the package.
     * @return `true`: success<br></br>`false`: fail
     */
    fun uninstallAppSilent(packageName: String): Boolean {
        return uninstallAppSilent(packageName, false)
    }

    /**
     * Uninstall the app silently.
     *
     * Without root permission must hold
     * `<uses-permission android:name="android.permission.DELETE_PACKAGES" />`
     *
     * @param packageName The name of the package.
     * @param isKeepData  Is keep the data.
     * @return `true`: success<br></br>`false`: fail
     */
    fun uninstallAppSilent(packageName: String, isKeepData: Boolean): Boolean {
        if (isSpace(packageName)) return false
        val isRoot = isDeviceRooted()
        val command = ("LD_LIBRARY_PATH=/vendor/lib*:/system/lib* pm uninstall "
                + (if (isKeepData) "-k " else "")
                + packageName)
        val commandResult = ShellUtils.execCmd(command, isRoot, true)
        if (commandResult.successMsg != null && commandResult.successMsg?.toLowerCase()!!.contains("success")) {
            return true
        } else {
            Log.e("AppUtils", "uninstallAppSilent successMsg: " + commandResult.successMsg +
                    ", errorMsg: " + commandResult.errorMsg)
            return false
        }
    }

    /**
     * Return whether the app is installed.
     *
     * @param action   The Intent action, such as ACTION_VIEW.
     * @param category The desired category.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isAppInstalled(action: String,
                       category: String): Boolean {
        val intent = Intent(action)
        intent.addCategory(category)
        val pm = BaseUtils.getApp().packageManager
        val info = pm.resolveActivity(intent, 0)
        return info != null
    }

    /**
     * Return whether the app is installed.
     *
     * @param packageName The name of the package.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isAppInstalled(packageName: String): Boolean {
        return !isSpace(packageName) && IntentUtils.getLaunchAppIntent(packageName) != null
    }


    /**
     * Return the application's information.
     *
     *  * name of package
     *  * icon
     *  * name
     *  * path of package
     *  * version name
     *  * version code
     *  * is system
     *
     *
     * @return the application's information
     */
    fun getAppInfo(): AppInfo? {
        return getAppInfo(BaseUtils.getApp().packageName)
    }

    /**
     * Return the application's information.
     *
     *  * name of package
     *  * icon
     *  * name
     *  * path of package
     *  * version name
     *  * version code
     *  * is system
     *
     *
     * @param packageName The name of the package.
     * @return 当前应用的 AppInfo
     */
    fun getAppInfo(packageName: String): AppInfo? {
        return try {
            val pm = BaseUtils.getApp().packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            getBean(pm, pi)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }

    }

    /**
     * Return the applications' information.
     *
     * @return the applications' information
     */
    fun getAppsInfo(): List<AppInfo> {
        val list = ArrayList<AppInfo>()
        val pm = BaseUtils.getApp().packageManager
        val installedPackages = pm.getInstalledPackages(0)
        for (pi in installedPackages) {
            val ai = getBean(pm, pi) ?: continue
            list.add(ai)
        }
        return list
    }

    private fun getBean(pm: PackageManager?, pi: PackageInfo?): AppInfo? {
        if (pm == null || pi == null) return null
        val ai = pi.applicationInfo
        val packageName = pi.packageName
        val name = ai.loadLabel(pm).toString()
        val icon = ai.loadIcon(pm)
        val packagePath = ai.sourceDir
        val versionName = pi.versionName
        val versionCode = pi.versionCode
        val isSystem = ApplicationInfo.FLAG_SYSTEM and ai.flags != 0
        return AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem)
    }

    private fun isFileExists(file: File?): Boolean {
        return file != null && file.exists()
    }

    private fun getFileByPath(filePath: String): File? {
        return if (isSpace(filePath)) null else File(filePath)
    }

    private fun isSpace(s: String?): Boolean {
        if (s == null) return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }

    private fun isDeviceRooted(): Boolean {
        val su = "su"
        val locations = arrayOf("/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/")
        for (location in locations) {
            if (File(location + su).exists()) {
                return true
            }
        }
        return false
    }

    private val HEX_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

    private fun encryptSHA1ToString(data: ByteArray): String? {
        return bytes2HexString(encryptSHA1(data))
    }

    private fun encryptSHA1(data: ByteArray?): ByteArray? {
        if (data == null || data.isEmpty()) return null
        try {
            val md = MessageDigest.getInstance("SHA1")
            md.update(data)
            return md.digest()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        }

    }

    private fun bytes2HexString(bytes: ByteArray?): String? {
        if (bytes == null) return null
        val len = bytes.size
        if (len <= 0) return null
        val ret = CharArray(len shl 1)
        var i = 0
        var j = 0
        while (i < len) {
            // TODO
//            ret[j++] = HEX_DIGITS[bytes[i].ushr(4) and 0x0f]
//            ret[j++] = HEX_DIGITS[bytes[i] and 0x0f]
//            i++
        }
        return String(ret)
    }

    /**
     * The application's information.
     */
    class AppInfo(packageName: String, name: String, icon: Drawable, packagePath: String,
                  versionName: String, versionCode: Int, isSystem: Boolean) {

        var packageName: String? = null
        var name: String? = null
        var icon: Drawable? = null
        var packagePath: String? = null
        var versionName: String? = null
        var versionCode: Int = 0
        var isSystem: Boolean = false

        init {
            this.name = name
            this.icon = icon
            this.packageName = packageName
            this.packagePath = packagePath
            this.versionName = versionName
            this.versionCode = versionCode
            this.isSystem = isSystem
        }

        override fun toString(): String {
            return "pkg name: " + packageName +
                    "\napp icon: " + icon +
                    "\napp name: " + name +
                    "\napp path: " + packagePath +
                    "\napp v name: " + versionName +
                    "\napp v code: " + versionCode +
                    "\nis system: " + isSystem
        }
    }

}