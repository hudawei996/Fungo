package com.fungo.repertory.image

import android.view.View
import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.baselib.utils.ToastUtils
import com.fungo.baseuilib.utils.ViewUtils
import com.fungo.imagego.ImageManager
import com.fungo.imagego.listener.OnImageSaveListener
import com.fungo.imagego.listener.OnProgressListener
import com.fungo.repertory.R
import kotlinx.android.synthetic.main.activity_image_main.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar

/**
 * @author Pinger
 * @since 3/29/18 6:55 PM
 * 图片展示选项
 */
class ImageMainActivity : BaseActivity() {

    private val mUrl by lazy {
        "http://c.hiphotos.baidu.com/image/pic/item/e1fe9925bc315c609e11bbb781b1cb13485477e6.jpg"
    }

    private val mGifUrl by lazy {
        "http://storage.slide.news.sina.com.cn/slidenews/77_ori/2018_18/74766_821684_756393.gif"
    }

    override val layoutResID: Int
        get() = R.layout.activity_image_main

    private var mCurrentProgress = 0
    private var mCurrentMenuId = 0

    override fun initView() {
        setActionBar(getString(R.string.image_loader), true)
        setCacheSize()

        ImageManager.instance.loadImage(mUrl, imageView)
    }

    private fun setCacheSize() {
        setText(tvImageSize, "图片缓存大小：" + ImageManager.instance.getImageCacheSize(this))
    }

    override fun initEvent() {
        setOnClick(tvImageClear)
        seekBar.setOnProgressChangeListener(object : DiscreteSeekBar.OnProgressChangeListener {
            override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {
            }

            override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
                mCurrentProgress = value
                if (mCurrentProgress % 2 == 0) {
                    onOptionsItemProgress(mCurrentMenuId, mCurrentProgress)
                }
            }
        })
    }

    override fun getMenuResID(): Int {
        return R.menu.menu_image_action
    }

    override fun onOptionsItemSelected(itemId: Int) {
        hideViews()
        mCurrentMenuId = itemId
        mCurrentProgress = -1
        onOptionsItemProgress(itemId, mCurrentProgress)
    }


    /** 隐藏不需要的View */
    private fun hideViews() {
        ViewUtils.setGone(seekBar)

    }

    private fun onOptionsItemProgress(itemId: Int, progress: Int) {
        when (itemId) {
            R.id.image_action_save -> {
                ImageManager.instance.saveImage(this, mUrl, object : OnImageSaveListener {
                    override fun onSaveSuccess(msg: String) {
                        ToastUtils.showToast(msg)
                    }

                    override fun onSaveFail(msg: String) {
                        ToastUtils.showToast(msg)
                    }
                })
            }
            R.id.image_action_progress -> {
                ImageManager.instance.loadGifImageWithProgress(mGifUrl, imageView, object : OnProgressListener {
                    override fun onProgress(bytesRead: Long, contentLength: Long, isDone: Boolean) {
                        ViewUtils.setVisible(circleProgressView)
                        circleProgressView.progress = (100f * bytesRead / contentLength).toInt()
                        if (isDone) ViewUtils.setGone(circleProgressView)
                    }
                })
            }
            R.id.image_action_linear -> {
                loadLinearImage()
            }
        }
    }

    private fun loadLinearImage() {
        ToastUtils.showToast(getString(R.string.image_action_linear))
    }

    override fun onClick(view: View) {
        when (view) {
            tvImageClear -> {
                ImageManager.instance.clearImageCache(this)
            }
        }
    }

}