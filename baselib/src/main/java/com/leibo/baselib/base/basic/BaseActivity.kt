package com.leibo.baselib.base.basic

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.NonNull
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.leibo.baselib.base.page.IBaseView

/**
 * @author Pinger
 * @since 2018/1/11 0011 上午 11:31
 * Activity基类，封装常用属性和方法
 */

abstract class BaseActivity : AppCompatActivity(), IBaseView {

    /**
     * 获取控件ID
     */
    protected abstract val layoutResID: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPre()
        setContentView(layoutResID)
        initView()
        initEvent()
        initData()
    }

    private fun initPre() {}
    protected open fun initView() {}
    protected open fun initEvent() {}
    protected open fun initData() {}

    override fun <T : View> findView(id: Int): T {
        return findViewById(id)
    }

    override fun setOnClick(view: View?) {
        view?.setOnClickListener(this)
    }

    override fun setOnClick(@IdRes id: Int) {
        setOnClick(findViewById<View>(id))
    }

    override fun setGone(@IdRes id: Int) {
        setGone(findViewById<View>(id))
    }

    override fun setGone(view: View?) {
        if (view != null && view.visibility != View.GONE) {
            view.visibility = View.GONE
        }
    }

    override fun setVisible(@IdRes id: Int) {
        setVisible(findViewById<View>(id))
    }

    override fun setVisible(view: View?) {
        if (view != null && view.visibility != View.VISIBLE) {
            view.visibility = View.VISIBLE
        }
    }

    override fun setVisibility(@IdRes id: Int, visibility: Int) {
        setVisibility(findViewById<View>(id), visibility)
    }

    override fun setVisibility(view: View?, visibility: Int) {
        view?.visibility = visibility
    }

    override fun setText(@IdRes id: Int, text: CharSequence?) {
        val textView = findViewById<View>(id) as TextView
        setText(textView, text)
    }

    override fun setText(@IdRes id: Int, @StringRes resId: Int) {
        setText(findViewById<View>(id) as TextView, resId)
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
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
    }

    override fun showToast(@StringRes resId: Int) {
        showToast(getString(resId))
    }

    override fun showLongToast(content: String?) {
        if (!TextUtils.isEmpty(content))
            Toast.makeText(this, content, Toast.LENGTH_LONG).show()
    }

    override fun showLongToast(@StringRes resId: Int) {
        showLongToast(getString(resId))
    }

    protected fun setActionBar(title: String, isBack: Boolean) {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = title
            actionBar.setDisplayHomeAsUpEnabled(isBack)
        }
    }

    protected fun startActivity(clazz: Class<*>) {
        startActivity(Intent(this, clazz))
    }

    override fun onClick(@NonNull view: View) {}
    override fun onClick(id: Int) {}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // 返回按键处理
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
