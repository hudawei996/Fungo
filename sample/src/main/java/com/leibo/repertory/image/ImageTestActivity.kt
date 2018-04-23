package com.leibo.repertory.image

import android.view.View
import android.widget.ImageView
import com.leibo.baselib.base.basic.BaseActivity
import com.leibo.baselib.image.ImageManager
import com.leibo.baselib.image.listener.ImageSaveListener
import com.leibo.baselib.image.progress.ProgressListener
import com.leibo.baselib.utils.ToastUtils
import com.leibo.baseuilib.utils.ViewUtils
import com.leibo.repertory.R
import kotlinx.android.synthetic.main.activity_image.*

/**
 * @author Pinger
 * @since 3/29/18 6:55 PM
 */
class ImageTestActivity : BaseActivity() {

    private val mUrl by lazy {
        "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1522328157&di=f9a39792eafb913012a6cb04f09f4be4&src=http://img01.taopic.com/160625/235106-1606250Q05845.jpg"
    }

    private val mGifUrl by lazy {
        "http://img.xieeo.com/allimg/140927/010H0DR-0.gif"
    }

    private val mImageView by lazy {
        findViewById<ImageView>(R.id.imageView)
    }

    override val layoutResID: Int
        get() = R.layout.activity_image


    override fun initView() {
        setActionBar(getString(R.string.image_loader), true)
        btnNormal.performClick()
    }

    override fun onClick(view: View) {
        when (view) {
            btnNormal -> ImageManager.instance.loadImage(mUrl, mImageView)
            btnNormalBlur -> ImageManager.instance.loadBlurImage(mUrl, mImageView, 8f)
            btnNormalCircle -> ImageManager.instance.loadCircleImage(mUrl, mImageView)
            btnNormalRound -> ImageManager.instance.loadRoundImage(mUrl, mImageView, 8)
            btnGif -> ImageManager.instance.loadImage(mGifUrl, mImageView)
            btnGifSave -> ImageManager.instance.saveImage(this, mGifUrl, object : ImageSaveListener {
                override fun onSaveSuccess(msg: String) {
                    ToastUtils.showToast(msg)
                }

                override fun onSaveFail(msg: String) {
                    ToastUtils.showToast(msg)
                }

            })
            btnNormalSave -> ImageManager.instance.saveImage(this, mUrl, object : ImageSaveListener {
                override fun onSaveSuccess(msg: String) {
                    ToastUtils.showToast(msg)
                }

                override fun onSaveFail(msg: String) {
                    ToastUtils.showToast(msg)
                }

            })

            btnNormalProgress -> {
                ImageManager.instance.loadImageWithProgress(mUrl, mImageView, object : ProgressListener {
                    override fun onProgress(bytesRead: Long, contentLength: Long, isDone: Boolean) {
                        ViewUtils.setVisible(progressView)
                        progressView.progress = ((bytesRead / contentLength) * 100f).toInt()
                        if (isDone) {
                            ViewUtils.setGone(progressView)
                        }
                    }
                })
            }
        }
    }

}