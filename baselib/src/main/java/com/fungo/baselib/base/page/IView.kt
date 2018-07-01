package com.fungo.baselib.base.page

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * @author pinger
 * @since 2018/1/13 23:18
 * View的一些基本操作行为，一般放在基类里实现
 *
 */
interface IView : View.OnClickListener {

    fun <T : View> findView(@IdRes id: Int): T

    /**
     * 点击事件
     */
    fun setOnClick(@IdRes id: Int)

    fun setOnClick(view: View?)
    fun onClick(@IdRes id: Int)
    override fun onClick(view: View) {
        onClick(view.id)
    }

    /**
     * View的可见与不可见
     */
    fun setVisibility(@IdRes id: Int, visibility: Int)

    fun setVisibility(view: View?, visibility: Int)
    fun setVisible(@IdRes id: Int)
    fun setVisible(view: View?)
    fun setGone(@IdRes id: Int)
    fun setGone(view: View?)

    /**
     * 设置文字
     */
    fun setText(@IdRes id: Int, text: CharSequence?)

    fun setText(@IdRes id: Int, @StringRes resId: Int)
    fun setText(textView: TextView?, text: CharSequence?)
    fun setText(textView: TextView?, @StringRes resId: Int)

    /**
     * 设置图片
     */
    fun setImage(imageView: ImageView?, url: String?)

    fun setImageResource(imageView: ImageView?, @DrawableRes resId: Int)
    fun setImageBitmap(imageView: ImageView?, bitmap: Bitmap?)
    fun setImageDrawable(imageView: ImageView?, drawable: Drawable?)

    /**
     * 展示吐司
     */
    fun showToast(content: String?)

    fun showToast(@StringRes resId: Int)
    fun showLongToast(content: String?)
    fun showLongToast(@StringRes resId: Int)

}