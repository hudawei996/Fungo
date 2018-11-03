package com.fungo.baseuilib.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import com.fungo.baseuilib.R
import com.fungo.baseuilib.basic.IView
import com.fungo.baseuilib.theme.UiUtils
import com.fungo.baseuilib.utils.StatusBarUtils


/**
 * @author Pinger
 * @since 2018/1/11 0011 上午 11:31
 *
 * Activity基类，封装常用属性和方法
 * 子类实现后，实现[layoutResID]设置页面布局
 * [initView]初始化布局
 * [initEvent]初始化事件
 * [initData]初始化数据
 */
abstract class BaseActivity : SupportActivity(), IView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPre()
        setContentView(layoutResID)
        initView()
        initEvent()
        initData()
    }

    /**
     * 提前初始化状态栏和主题颜色等属性
     */
    private fun initPre() {
        // 沉浸式
        if (isStatusBarTranslate()) StatusBarUtils.setStatusBarTranslucent(this)
        // 主题
        UiUtils.setCurrentTheme(this, UiUtils.getCurrentTheme(this))
        // 设置状态栏背景颜色
        StatusBarUtils.setStatusBarForegroundColor(this, isStatusBarForegroundBlack())
        // 设置是否可以滑动返回，默认可以
        setSwipeBackEnable(isSwipeBackEnable())
        // 设置Fragment的默认背景颜色
        setDefaultFragmentBackground(R.color.grey_f7)
    }

    /**
     * 页面布局的id
     */
    abstract val layoutResID: Int

    /**
     * 子类初始化View
     * 可以使用[findView]查找View的id
     */
    protected open fun initView() {}

    /**
     * 初始化控件的事件
     */
    protected open fun initEvent() {}

    /**
     * 初始化数据
     */
    protected open fun initData() {}

    /**
     * 获取菜单项资源ID
     */
    protected open fun getMenuResID(): Int = 0

    /**
     * 菜单项点击
     */
    protected open fun onMenuItemSelected(itemId: Int): Boolean = true

    /**
     * 状态栏是否沉浸
     */
    protected open fun isStatusBarTranslate(): Boolean = true

    /**
     * 状态栏前景色是否是黑色
     */
    protected open fun isStatusBarForegroundBlack(): Boolean = true

    /**
     * 是否可以滑动返回，默认不可以，如果想滑动返回可以重写该方法
     * 或者继承[BaseSwipeBackActivity]
     */
    protected open fun isSwipeBackEnable(): Boolean = false

    /**
     * 根据id查找View
     */
    override fun <T : View> findView(id: Int): T {
        return findViewById(id)
    }

    /**
     * Activity跳转
     */
    protected fun startActivity(clazz: Class<*>) {
        startActivity(Intent(this, clazz))
    }

    // --------------View常用的方法---------------
    // --------------View常用的方法---------------
    // --------------View常用的方法---------------
    override fun getContext(): Context? {
        return this
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
        if (view?.visibility != View.GONE) {
            view?.visibility = View.GONE
        }
    }

    override fun setVisible(@IdRes id: Int) {
        setVisible(findView<View>(id))
    }

    override fun setVisible(view: View?) {
        if (view?.visibility != View.VISIBLE) {
            view?.visibility = View.VISIBLE
        }
    }

    override fun setVisibility(@IdRes id: Int, visibility: Int) {
        setVisibility(findView<View>(id), visibility)
    }

    override fun setVisibility(view: View?, visibility: Int) {
        view?.visibility = visibility
    }

    override fun setVisibility(id: Int, isVisible: Boolean) {
        setVisibility(findView<View>(id), isVisible)
    }

    override fun setVisibility(view: View?, isVisible: Boolean) {
        if (isVisible) setVisible(view)
        else setGone(view)
    }

    override fun setText(@IdRes id: Int, text: CharSequence?) {
        setText(findView<TextView>(id), text)
    }

    override fun setText(@IdRes id: Int, @StringRes resId: Int) {
        setText(findView<TextView>(id), resId)
    }

    override fun setText(textView: TextView?, @StringRes resId: Int) {
        setText(textView, getString(resId))
    }

    override fun setText(textView: TextView?, text: CharSequence?) {
        textView?.text = text
    }

    override fun setImageResource(imageView: ImageView?, @DrawableRes resId: Int) {
        imageView?.setImageResource(resId)
    }

    override fun setImageBitmap(imageView: ImageView?, bitmap: Bitmap?) {
        imageView?.setImageBitmap(bitmap)
    }

    override fun setImageDrawable(imageView: ImageView?, drawable: Drawable?) {
        imageView?.setImageDrawable(drawable)
    }

    override fun showToast(content: String?) {
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
            // 返回按键处理
            onBackPressedSupport()
            return true
        }
        return onMenuItemSelected(item.itemId)
    }
}
