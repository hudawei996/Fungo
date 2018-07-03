package com.fungo.baselib.base.basic

import android.app.ProgressDialog
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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.fungo.baselib.base.page.IView


/**
 * @author Pinger
 * @since 2018/1/11 0011 上午 11:31
 * Activity基类，封装常用属性和方法
 */

abstract class BaseActivity : AppCompatActivity(), IView {

    /**
     * 获取控件ID
     */
    protected abstract val layoutResID: Int
    private var mProgressDialog: ProgressDialog? = null

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

    fun showProgress() {
       showProgress("加载中...")
    }

    fun showProgress(msg:String) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog?.setMessage(msg)
        }
        if (mProgressDialog?.isShowing == true || this.isFinishing) {
            return
        }
        mProgressDialog?.show()
    }

    fun dismissProgress() {
        mProgressDialog?.dismiss()
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

    protected fun setToolBar(title: String, isBack: Boolean) {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return if (getMenuResID() == 0) {
            super.onCreateOptionsMenu(menu)
        } else {
            menuInflater.inflate(getMenuResID(), menu)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            finish() // 返回按键处理
            return true
        }
        onMenuItemSelected(item.itemId)
        return true
    }

    /** 获取菜单项资源ID */
    open fun getMenuResID(): Int {
        return 0
    }

    /** 菜单项点击 */
    open fun onMenuItemSelected(itemId: Int) {}


}
