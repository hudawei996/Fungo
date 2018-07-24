package com.fungo.baselib.base.basic

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.fungo.baselib.utils.StatusBarUtils


/**
 * @author Pinger
 * @since 2018/1/11 0011 上午 11:31
 * Activity基类，封装常用属性和方法
 */

abstract class BaseActivity : AppCompatActivity() {

    /**
     * 获取控件ID
     */
    protected abstract val layoutResID: Int
    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPre()
        setContentView(layoutResID)
        initAfter()
        initView()
        initEvent()
        initData()
    }

    protected open fun initView() {}
    protected open fun initEvent() {}
    protected open fun initData() {}


    protected open fun initPre() {
        if (isStatusBarTranslate()) StatusBarUtils.setStatusBarTranslucent(this)
        StatusBarUtils.setStatusBarForegroundColor(this, isStatusBarForegroundBlack())
    }

    protected open fun initAfter() {
        if (isStatusBarTranslate()) StatusBarUtils.setStatusBarHeight(getStatusBarView())
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
     * 获取状态栏View
     */
    protected open fun getStatusBarView(): View? = null


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


}
