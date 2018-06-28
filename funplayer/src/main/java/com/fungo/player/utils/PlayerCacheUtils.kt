package com.fungo.player.utils

import android.content.Context
import android.os.Environment
import com.danikula.videocache.HttpProxyCacheServer
import com.danikula.videocache.file.Md5FileNameGenerator
import com.fungo.player.R
import java.io.File

/**
 * @author Pinger
 * @since 18-6-13 下午4:46
 * 视频播放缓存工具类
 */

object PlayerCacheUtils {

    private var mSharedProxy: HttpProxyCacheServer? = null


    fun getProxy(context: Context): HttpProxyCacheServer {
        if (mSharedProxy == null) {
            mSharedProxy = newProxy(context)
        }
        return mSharedProxy!!
    }

    private fun newProxy(context: Context): HttpProxyCacheServer {
        return HttpProxyCacheServer.Builder(context)
                .maxCacheSize((1024 * 1024 * 1024).toLong())       // 1 Gb for cache
                .build()
    }


    /**
     * 删除所有缓存文件
     * @return 返回缓存是否删除成功
     */
    fun clearAllCache(context: Context): Boolean {
        val cacheDirectory = getIndividualCacheDirectory(context)
        return deleteFiles(cacheDirectory)
    }

    /**
     * 删除url对应默认缓存文件
     * @return 返回缓存是否删除成功
     */
    fun clearDefaultCache(context: Context, url: String): Boolean {
        val md5FileNameGenerator = Md5FileNameGenerator()
        val name = md5FileNameGenerator.generate(url)
        val pathTmp = (getIndividualCacheDirectory(context.applicationContext).getAbsolutePath()
                + File.separator + name + ".download")
        val path = (getIndividualCacheDirectory(context.applicationContext).getAbsolutePath()
                + File.separator + name)
        return deleteFile(pathTmp) && deleteFile(path)
    }


    /**
     * Returns individual application cache directory (for only video caching from Proxy). Cache directory will be
     * created on SD card *("/Android/data/[app_package_name]/cache/video-cache")* if card is mounted .
     * Else - Android defines cache directory on device's file system.
     *
     * @param context Application context
     * @return Cache [directory][File]
     */
    fun getIndividualCacheDirectory(context: Context): File {
        val cacheDir = getCacheDirectory(context, true)
        return File(cacheDir, context.getString(R.string.player_name))
    }

    /**
     * Returns application cache directory. Cache directory will be created on SD card
     * *("/Android/data/[app_package_name]/cache")* (if card is mounted and app has appropriate permission) or
     * on device's file system depending incoming parameters.
     *
     * @param context        Application context
     * @param preferExternal Whether prefer external location for cache
     * @return Cache [directory][File].<br></br>
     * **NOTE:** Can be null in some unpredictable cases (if SD card is unmounted and
     * [Context.getCacheDir()][Context.getCacheDir] returns null).
     */
    private fun getCacheDirectory(context: Context, preferExternal: Boolean): File {
        var appCacheDir: File? = null
        val externalStorageState: String = try {
            Environment.getExternalStorageState()
        } catch (e: NullPointerException) { // (sh)it happens
            ""
        }

        if (preferExternal && Environment.MEDIA_MOUNTED == externalStorageState) {
            appCacheDir = getExternalCacheDir(context)
        }
        if (appCacheDir == null) {
            appCacheDir = context.cacheDir
        }
        if (appCacheDir == null) {
            val cacheDirPath = context.filesDir.path + context.packageName + "/cache/"
            PlayerLogUtils.e("Can't define system cache directory! '$cacheDirPath%s' will be used.")
            appCacheDir = File(cacheDirPath)
        }
        return appCacheDir
    }

    private fun getExternalCacheDir(context: Context): File? {
        val dataDir = File(File(Environment.getExternalStorageDirectory(), "Android"), "data")
        val appCacheDir = File(File(dataDir, context.packageName), "cache")
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                PlayerLogUtils.e("Unable to create external cache directory")
                return null
            }
        }
        return appCacheDir
    }

    /**
     * delete directory
     */
    private fun deleteFiles(root: File): Boolean {
        val files = root.listFiles()
        if (files != null) {
            for (f in files) {
                if (!f.isDirectory && f.exists()) { // 判断是否存在
                    if (!f.delete()) {
                        return false
                    }
                }
            }
        }
        return true
    }

    /**
     * delete file
     */
    private fun deleteFile(filePath: String): Boolean {
        val file = File(filePath)
        if (file.exists()) {
            if (file.isFile) {
                if (!file.delete()) {
                    return false
                }
            } else {
                val filePaths = file.list()
                for (path in filePaths) {
                    deleteFile(filePath + File.separator + path)
                }
                if (!file.delete()) {
                    return false
                }
            }
        }
        return true
    }


}