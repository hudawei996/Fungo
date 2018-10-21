package com.fungo.sample.imagego

import android.graphics.Bitmap
import android.view.View
import com.fungo.baselib.base.page.BasePageFragment
import com.fungo.imagego.*
import com.fungo.imagego.glide.GlideImageStrategy
import com.fungo.imagego.listener.OnImageListener
import com.fungo.imagego.listener.OnProgressListener
import com.fungo.imagego.strategy.ImageGoEngine
import com.fungo.sample.R
import kotlinx.android.synthetic.main.fragment_imagego.*

/**
 * @author Pinger
 * @since 2018/10/16 1:40
 */
class ImageGoFragment : BasePageFragment() {

    private val mUrl = "http://img.mp.itc.cn/upload/20161121/d30e0a4a1f8b418f92e973310885e4ee_th.jpg"
    private val mGif = "http://www.gaoxiaogif.com/d/file/201712/ac2cba0163c27c0f455c22df35794bc8.gif"

    private var isGif = false


    override fun getPageTitle(): String? {
        return getString(R.string.main_imagego)
    }

    override fun getPageLayoutResId(): Int {
        return R.layout.fragment_imagego
    }

    override fun initPageView() {

        rgStrategy.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbGlide -> ImageGoEngine.setImageStrategy(GlideImageStrategy())
            }
        }

        rgType.setOnCheckedChangeListener { _, checkedId ->
            // 手动区分链接
            isGif = checkedId == R.id.rbGif
            // 自动设置区分gif加载
            ImageGoEngine.setAutoGif(checkedId == R.id.rbGif)
        }

        rbNormal.isChecked = true
        rbGlide.isChecked = true

        onLoadRound()
    }

    override fun initEvent() {
        setOnClick(tvOrigin)
        setOnClick(tvBitmap)
        setOnClick(tvCircle)
        setOnClick(tvRound)
        setOnClick(tvBlur)
        setOnClick(tvProgress)
        setOnClick(tvSave)
    }

    private fun getUrl(): String {
        return if (isGif) {
            mGif
        } else mUrl
    }

    override fun onClick(view: View) {
        when (view) {
            tvOrigin -> onLoadOrigin()
            tvBitmap -> onLoadBitmap()
            tvCircle -> onLoadCircle()
            tvRound -> onLoadRound()
            tvBlur -> onLoadBlur()
            tvProgress -> onLoadProgress()
            tvSave -> onLoadSave()
        }
    }

    private fun onLoadOrigin() {
        loadImage(getUrl(), imageView)
    }


    private fun onLoadBitmap() {
        loadBitmap(context, getUrl(), object : OnImageListener {
            override fun onSuccess(bitmap: Bitmap?) {
                showToast("Bitmap加载成功")
                imageView.setImageBitmap(bitmap)
            }

            override fun onFail(msg: String?) {
                showToast(msg)
            }

        })
    }


    private fun onLoadCircle() {
        loadCircle(getUrl(), imageView)
    }


    private fun onLoadRound() {
        loadRound(getUrl(), imageView, 48)
    }

    private fun onLoadBlur() {
        loadBlur(getUrl(), imageView)
    }


    /**
     * 展示进度条
     */
    private fun onLoadProgress() {
        loadProgress(getUrl(), imageView, object : OnProgressListener {
            override fun onProgress(bytesRead: Long, contentLength: Long, isFinish: Boolean) {
                progressView.visibility = if (isFinish) View.GONE else View.VISIBLE
                progressView.progress = (100f * bytesRead / contentLength).toInt()
            }
        })
    }


    /**
     * 保存图片
     */
    private fun onLoadSave() {
        saveImage(context, getUrl())
    }

}