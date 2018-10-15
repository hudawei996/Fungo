package com.fungo.baselib.base.basic

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.fungo.baselib.R
import com.fungo.baselib.utils.StatusBarUtils
import com.fungo.baselib.theme.ThemeHelper
import com.fungo.baselib.theme.AppTheme


/**
 * @author Pinger
 * @since 2018/1/11 0011 上午 11:31
 * Activity基类，封装常用属性和方法
 */

abstract class BaseActivity : SupportActivity() {

    /**
     * 获取控件ID
     */
    abstract val layoutResID: Int
    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPre()
        setContentView(layoutResID)
        initView()
        initEvent()
        initData()
    }

    protected open fun initView() {}
    protected open fun initEvent() {}
    protected open fun initData() {}

    private fun initPre() {
        // 主题
        initTheme()
        // 沉浸式
        if (isStatusBarTranslate()) StatusBarUtils.setStatusBarTranslucent(this)
        // 设置状态栏背景颜色
        StatusBarUtils.setStatusBarForegroundColor(this, isStatusBarForegroundBlack())

        // 设置是否可以滑动返回，默认可以
        setSwipeBackEnable(isSwipeBackEnable())
    }


    protected fun <T : View> findView(id: Int): T {
        return findViewById(id)
    }

    fun showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog?.setMessage("加载中...")
        }
        if (mProgressDialog?.isShowing == true || this.isFinishing) {
            return
        }
        mProgressDialog?.show()
    }

    fun dismissProgress() {
        mProgressDialog?.dismiss()
    }


    protected fun startActivity(clazz: Class<*>) {
        startActivity(Intent(this, clazz))
    }

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


    /**
     * 获取菜单项资源ID
     */
    protected open fun getMenuResID(): Int = 0

    /**
     * 菜单项点击
     */
    protected open fun onMenuItemSelected(itemId: Int) {}


    /**
     * 状态栏是否沉浸
     */
    protected open fun isStatusBarTranslate(): Boolean = true

    /**
     * 状态栏前景色是否是黑色
     */
    protected open fun isStatusBarForegroundBlack(): Boolean = true

    /**
     * 是否可以滑动返回，默认可以
     */
    protected open fun isSwipeBackEnable(): Boolean = true


    /**
     * 设置系统的主题
     */
    private fun initTheme() {
        when (ThemeHelper.getCurrentTheme()) {
            AppTheme.Blue -> setTheme(R.style.BlueTheme)
            AppTheme.Red -> setTheme(R.style.RedTheme)
            AppTheme.Brown -> setTheme(R.style.BrownTheme)
            AppTheme.Green -> setTheme(R.style.GreenTheme)
            AppTheme.Purple -> setTheme(R.style.PurpleTheme)
            AppTheme.Teal -> setTheme(R.style.TealTheme)
            AppTheme.Pink -> setTheme(R.style.PinkTheme)
            AppTheme.DeepPurple -> setTheme(R.style.DeepPurpleTheme)
            AppTheme.Orange -> setTheme(R.style.OrangeTheme)
            AppTheme.Indigo -> setTheme(R.style.IndigoTheme)
            AppTheme.LightGreen -> setTheme(R.style.LightGreenTheme)
            AppTheme.Lime -> setTheme(R.style.LimeTheme)
            AppTheme.DeepOrange -> setTheme(R.style.DeepOrangeTheme)
            AppTheme.Cyan -> setTheme(R.style.CyanTheme)
            AppTheme.BlueGrey -> setTheme(R.style.BlueGreyTheme)
            else -> setTheme(R.style.BlueTheme)
        }
    }

}
