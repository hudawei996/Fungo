package com.leibo.repertory.image

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.widget.ImageView
import com.leibo.baselib.base.BaseActivity
import com.leibo.baselib.utils.PermissionUtils
import com.leibo.repertory.R
import org.fungo.baselib.image.ImageManager

/**
 * @author Pinger
 * @since 3/29/18 6:55 PM
 */
class ImageTestActivity : BaseActivity() {


    private val mImageView by lazy {
        findViewById<ImageView>(R.id.imageView)
    }

    override val layoutResID: Int
        get() = R.layout.activity_image


    override fun initView() {
        val url = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1522328157&di=f9a39792eafb913012a6cb04f09f4be4&src=http://img01.taopic.com/160625/235106-1606250Q05845.jpg"
        ImageManager.instance.loadBlurImage(url, mImageView, 10f)


        PermissionUtils.permission(arrayOf(WRITE_EXTERNAL_STORAGE)).callback(object : PermissionUtils.SimpleCallback {
            override fun onGranted() {

            }

            override fun onDenied() {
            }

        }).request()
    }

}