package com.fungo.repertory.image

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.baselib.utils.ToastUtils
import com.fungo.baseuilib.utils.ViewUtils
import com.fungo.baseuilib.view.round.CircleImageView
import com.fungo.baseuilib.view.round.RoundImageView
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

    private var mImageVIew: ImageView? = null
    private var mCircleImageView: CircleImageView? = null
    private var mRoundImageView: RoundImageView? = null

    override fun initView() {
        setActionBar(getString(R.string.image_loader), true)
        setCacheSize()
        ImageManager.instance.loadImage(mUrl, generateImageView())
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
                onMenuItemProgress(mCurrentMenuId)

            }
        })
    }

    override fun getMenuResID(): Int {
        return R.menu.menu_image_action
    }

    override fun onMenuItemSelected(itemId: Int) {
        handleViews()
        mCurrentMenuId = itemId
        mCurrentProgress = -1
        onMenuItemProgress(itemId)
    }

    /** 处理初始状态 */
    private fun handleViews() {
        ViewUtils.setGone(seekBar)
        mCircleImageView = null
        mRoundImageView = null
        mImageVIew = null
    }

    private fun onMenuItemProgress(itemId: Int) {
        when (itemId) {
            R.id.image_action_round -> setRoundImageView()
            R.id.image_action_circle -> setCircleImageView()
            R.id.image_action_save -> saveImageView()
            R.id.image_action_progress -> setImageProgress()
            R.id.image_action_linear -> loadLinearImage()

        }
    }


    private fun setImageProgress() {
        ImageManager.instance.loadImageWithProgress(mGifUrl, mImageVIew, object : OnProgressListener {
            override fun onProgress(bytesRead: Long, contentLength: Long, isDone: Boolean) {
                ViewUtils.setVisible(circleProgressView)
                circleProgressView.progress = (100f * bytesRead / contentLength).toInt()
                if (isDone) ViewUtils.setGone(circleProgressView)
            }
        })
    }

    private fun saveImageView() {
        ImageManager.instance.saveImage(this, mUrl, object : OnImageSaveListener {
            override fun onSaveSuccess(msg: String) {
                ToastUtils.showToast(msg)
            }

            override fun onSaveFail(msg: String) {
                ToastUtils.showToast(msg)
            }
        })
    }

    private fun setRoundImageView() {
        if (mCurrentProgress == -1) {
            setActionBar(getString(R.string.image_action_round), true)
            ViewUtils.setVisible(seekBar)
            mCurrentProgress = 18
            seekBar.min = 0
            seekBar.max = 100
            seekBar.progress = mCurrentProgress
            ImageManager.instance.loadImage(mUrl, generateRoundImageView())
        }
        mRoundImageView?.setRadius(mCurrentProgress)
    }


    private fun setCircleImageView() {
        if (mCurrentProgress == -1) {
            setActionBar(getString(R.string.image_action_circle), true)
            ViewUtils.setVisible(seekBar)
            mCurrentProgress = 250
            seekBar.min = 0
            seekBar.max = 500
            seekBar.progress = mCurrentProgress
        }
        ImageManager.instance.loadImage(mUrl, generateCircleImageView())
    }

    private fun loadLinearImage() {
        ToastUtils.showToast(getString(R.string.image_action_linear))
    }

    override fun onClick(view: View) {
        when (view) {
            tvImageClear -> {
                ImageManager.instance.clearImageCache(this)
                setCacheSize()
            }
        }
    }

    private fun generateImageView(): ImageView {
        if (mImageVIew == null) {
            mImageVIew = ImageView(this)
            mImageVIew!!.layoutParams = generateParams()
            mImageVIew!!.scaleType = ImageView.ScaleType.CENTER_CROP
            mImageVIew!!.isClickable = true
            container.removeAllViews()
            container.addView(mImageVIew)
        }
        return mImageVIew!!
    }


    private fun generateCircleImageView(): CircleImageView {
        if (mCircleImageView == null) {
            mCircleImageView = CircleImageView(this)
            mCircleImageView!!.scaleType = ImageView.ScaleType.CENTER_CROP
            container.removeAllViews()
            container.addView(mCircleImageView)
        }
        mCircleImageView!!.layoutParams = generateParams(mCurrentProgress, mCurrentProgress)
        return mCircleImageView!!
    }

    private fun generateRoundImageView(): RoundImageView {
        if (mRoundImageView == null) {
            mRoundImageView = RoundImageView(this)
            mRoundImageView!!.layoutParams = generateParams()
            mRoundImageView!!.scaleType = ImageView.ScaleType.CENTER_CROP
            mRoundImageView!!.isClickable = true
            container.removeAllViews()
            container.addView(mRoundImageView)
        }
        return mRoundImageView!!
    }

    private fun generateParams(): FrameLayout.LayoutParams {
        return generateParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }


    private fun generateParams(width: Int, height: Int): FrameLayout.LayoutParams {
        val params = FrameLayout.LayoutParams(width, height)
        params.gravity = Gravity.CENTER
        return params
    }

}