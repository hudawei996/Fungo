package com.fungo.baselib.base.basic

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.NonNull
import android.support.annotation.StringRes
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.fungo.baselib.base.page.IView

/**
 * @author pinger
 * @since 2018/1/13 23:52
 */
abstract class BaseFragment : SupportFragment(), IView {

    private var mRootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mRootView == null) mRootView = inflater.inflate(getLayoutResId(), container, false)
        val parent = mRootView!!.parent as? ViewGroup?
        parent?.removeView(mRootView)
        return if (isSwipeBack()) attachToSwipeBack(mRootView) else mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initPageView()
        initView()
        initEvent()
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        initData()
    }

    protected open fun initPageView() {}
    protected open fun initView() {}
    protected open fun initEvent() {}
    protected open fun initData() {}

    /**
     * 获取资源ID
     */
    abstract fun getLayoutResId(): Int


    override fun <T : View> findView(id: Int): T {
        return View(activity) as T
    }

    override fun setOnClick(view: View?) {
        view?.setOnClickListener(this)
    }

    override fun setOnClick(@IdRes id: Int) {
        setOnClick(findView<View>(id))
    }

    override fun setGone(@IdRes id: Int) {
        setGone(findView<View>(id))
    }

    override fun setGone(view: View?) {
        if (view != null && view.visibility != View.GONE) {
            view.visibility = View.GONE
        }
    }

    override fun setVisible(@IdRes id: Int) {
        setVisible(findView<View>(id))
    }

    override fun setVisible(view: View?) {
        if (view != null && view.visibility != View.VISIBLE) {
            view.visibility = View.VISIBLE
        }
    }

    override fun setVisibility(@IdRes id: Int, visibility: Int) {
        setVisibility(findView<View>(id), visibility)
    }

    override fun setVisibility(view: View?, visibility: Int) {
        view?.visibility = visibility
    }

    override fun setText(@IdRes id: Int, text: CharSequence?) {
        val textView = findView<View>(id) as TextView
        setText(textView, text)
    }

    override fun setText(@IdRes id: Int, @StringRes resId: Int) {
        setText(findView<View>(id) as TextView, resId)
    }

    override fun setText(textView: TextView?, @StringRes resId: Int) {
        setText(textView, getString(resId))
    }

    override fun setText(textView: TextView?, text: CharSequence?) {
        if (textView != null && !TextUtils.isEmpty(text)) {
            textView.text = text
        }
    }

    override fun setImage(imageView: ImageView?, url: String?) {
        if (imageView != null && !TextUtils.isEmpty(url)) {
        }
    }

    override fun setImageResource(imageView: ImageView?, @DrawableRes resId: Int) {
        imageView?.setImageResource(resId)
    }

    override fun setImageBitmap(imageView: ImageView?, bitmap: Bitmap?) {
        if (imageView != null && bitmap != null) {
            imageView.setImageBitmap(bitmap)
        }
    }

    override fun setImageDrawable(imageView: ImageView?, drawable: Drawable?) {
        if (imageView != null && drawable != null) {
            imageView.setImageDrawable(drawable)
        }
    }

    override fun showToast(content: String?) {
        if (!TextUtils.isEmpty(content))
            Toast.makeText(activity, content, Toast.LENGTH_SHORT).show()
    }

    override fun showToast(@StringRes resId: Int) {
        showToast(getString(resId))
    }

    override fun showLongToast(content: String?) {
        if (!TextUtils.isEmpty(content))
            Toast.makeText(activity, content, Toast.LENGTH_LONG).show()
    }

    override fun showLongToast(@StringRes resId: Int) {
        showLongToast(getString(resId))
    }

    /**
     * 子类自己实现点击事件
     */
    override fun onClick(@NonNull view: View) {}

    override fun onClick(id: Int) {}

    /**
     * 是否支持侧滑返回
     * 默认是可以侧滑返回的
     */
    protected open fun isSwipeBack() = true

}