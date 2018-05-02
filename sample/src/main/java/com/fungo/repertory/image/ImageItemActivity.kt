package com.fungo.repertory.image

import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.baselib.image.ImageManager
import com.fungo.baselib.image.progress.ProgressListener
import com.fungo.baseuilib.utils.ViewUtils
import com.fungo.repertory.R
import com.fungo.repertory.constant.IntentConstant
import kotlinx.android.synthetic.main.activity_image_item.*

/**
 * @author Pinger
 * @since 2018/5/2 22:30
 * 图片展示详细页面
 */

class ImageItemActivity : BaseActivity() {

    private val mUrl by lazy {
        "http://c.hiphotos.baidu.com/image/pic/item/e1fe9925bc315c609e11bbb781b1cb13485477e6.jpg"
    }

    private val mGifUrl by lazy {
        "http://storage.slide.news.sina.com.cn/slidenews/77_ori/2018_18/74766_821684_756393.gif"
    }

    private val mPosition by lazy {
        intent.getIntExtra(IntentConstant.IMAGE_ITEM_POSITION, 0)
    }

    override val layoutResID: Int
        get() = R.layout.activity_image_item


    override fun initView() {
        setActionBar(resources.getStringArray(R.array.image_action)[mPosition], true)

        when (mPosition) {
            0 -> {
                ImageManager.instance.loadImage(mUrl, imageView)
            }
            1 -> {
                ImageManager.instance.loadRoundImage(mUrl, imageView, 8)
            }

            8 -> {
                ImageManager.instance.loadGifImageWithProgress(mGifUrl, imageView, object : ProgressListener {
                    override fun onProgress(bytesRead: Long, contentLength: Long, isDone: Boolean) {
                        ViewUtils.setVisible(circleProgressView)
                        circleProgressView.progress = (100f * bytesRead / contentLength).toInt()
                        println("----> Progress = ${circleProgressView.progress}")

                        if (isDone) ViewUtils.setGone(circleProgressView)
                    }
                })
            }
        }

    }
}