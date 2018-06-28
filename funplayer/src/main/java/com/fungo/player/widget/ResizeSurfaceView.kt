package com.fungo.player.widget

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView
import android.view.View
import com.fungo.player.utils.PlayerConstant

/**
 * 可以更新大小的SurfaceView
 */

class ResizeSurfaceView : SurfaceView {

    private var mVideoWidth: Int = 0
    private var mVideoHeight: Int = 0
    private var screenType: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    fun setVideoSize(width: Int, height: Int) {
        mVideoWidth = width
        mVideoHeight = height
        holder.setFixedSize(width, height)
    }

    fun setScreenScale(type: Int) {
        screenType = type
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthSpec = widthMeasureSpec
        var heightSpec = heightMeasureSpec
        if (rotation == 90f || rotation == 270f) {
            widthSpec += heightSpec
            heightSpec = widthSpec - heightSpec
            widthSpec -= heightSpec
        }

        var width = View.getDefaultSize(mVideoWidth, widthSpec)
        var height = View.getDefaultSize(mVideoHeight, heightSpec)


        //如果设置了比例
        when (screenType) {
            PlayerConstant.SCREEN_SCALE_ORIGINAL -> {
                width = mVideoWidth
                height = mVideoHeight
            }
            PlayerConstant.SCREEN_SCALE_16_9 -> if (height > width / 16 * 9) {
                height = width / 16 * 9
            } else {
                width = height / 9 * 16
            }
            PlayerConstant.SCREEN_SCALE_4_3 -> if (height > width / 4 * 3) {
                height = width / 4 * 3
            } else {
                width = height / 3 * 4
            }
            PlayerConstant.SCREEN_SCALE_MATCH_PARENT -> {
                width = widthSpec
                height = heightSpec
            }
            PlayerConstant.SCREEN_SCALE_CENTER_CROP -> if (mVideoWidth > 0 && mVideoHeight > 0) {
                if (mVideoWidth * height > width * mVideoHeight) {
                    width = height * mVideoWidth / mVideoHeight
                } else {
                    height = width * mVideoHeight / mVideoWidth
                }
            }
            else -> if (mVideoWidth > 0 && mVideoHeight > 0) {

                val widthSpecMode = View.MeasureSpec.getMode(widthSpec)
                val widthSpecSize = View.MeasureSpec.getSize(widthSpec)
                val heightSpecMode = View.MeasureSpec.getMode(heightSpec)
                val heightSpecSize = View.MeasureSpec.getSize(heightSpec)

                if (widthSpecMode == View.MeasureSpec.EXACTLY && heightSpecMode == View.MeasureSpec.EXACTLY) {
                    // the size is fixed
                    width = widthSpecSize
                    height = heightSpecSize

                    // for compatibility, we adjust size based on aspect ratio
                    if (mVideoWidth * height < width * mVideoHeight) {
                        width = height * mVideoWidth / mVideoHeight
                    } else if (mVideoWidth * height > width * mVideoHeight) {
                        height = width * mVideoHeight / mVideoWidth
                    }
                } else if (widthSpecMode == View.MeasureSpec.EXACTLY) {
                    // only the width is fixed, adjust the height to match aspect ratio if possible
                    width = widthSpecSize
                    height = width * mVideoHeight / mVideoWidth
                    if (heightSpecMode == View.MeasureSpec.AT_MOST && height > heightSpecSize) {
                        // couldn't match aspect ratio within the constraints
                        height = heightSpecSize
                    }
                } else if (heightSpecMode == View.MeasureSpec.EXACTLY) {
                    // only the height is fixed, adjust the width to match aspect ratio if possible
                    height = heightSpecSize
                    width = height * mVideoWidth / mVideoHeight
                    if (widthSpecMode == View.MeasureSpec.AT_MOST && width > widthSpecSize) {
                        // couldn't match aspect ratio within the constraints
                        width = widthSpecSize
                    }
                } else {
                    // neither the width nor the height are fixed, try to use actual video size
                    width = mVideoWidth
                    height = mVideoHeight
                    if (heightSpecMode == View.MeasureSpec.AT_MOST && height > heightSpecSize) {
                        // too tall, decrease both width and height
                        height = heightSpecSize
                        width = height * mVideoWidth / mVideoHeight
                    }
                    if (widthSpecMode == View.MeasureSpec.AT_MOST && width > widthSpecSize) {
                        // too wide, decrease both width and height
                        width = widthSpecSize
                        height = width * mVideoHeight / mVideoWidth
                    }
                }
            }
        }
        setMeasuredDimension(width, height)
    }
}
