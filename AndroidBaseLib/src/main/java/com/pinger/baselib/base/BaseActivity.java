package com.pinger.baselib.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Pinger
 * @since 2018/1/11 0011 上午 11:31
 * Activity基类，封装常用属性和方法
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPre();
        setContentView(getLayoutResID());
        initView();
        initEvent();
        initData();
    }

    /**
     * 开始之前的一些初始化
     */
    private void initPre() {

    }

    /**
     * 获取控件ID
     */
    protected abstract int getLayoutResID();

    /**
     * 初始化视图
     */
    protected void initView() {
    }

    /**
     * 初始化事件
     */
    protected void initEvent() {

    }

    /**
     * 初始化事件
     */
    protected void initData() {

    }

    protected void setOnClick(View view) {
        if (view != null) view.setOnClickListener(this);
    }

    protected void setOnClick(@IdRes int id) {
        View view = findViewById(id);
        setOnClick(view);
    }

    /**
     * 控件隐藏
     */
    protected void setGone(@IdRes int id) {
        setGone(findViewById(id));
    }

    protected void setGone(View view) {
        if (view != null && view.getVisibility() != View.GONE) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 控件可见
     */
    protected void setVisible(@IdRes int id) {
        setVisible(findViewById(id));
    }

    protected void setVisible(View view) {
        if (view != null && view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置控件可见或不可见属性
     */
    protected void setVisibility(@IdRes int id, int visibility) {
        setVisibility(findViewById(id), visibility);
    }

    protected void setVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    protected void setText(@IdRes int id, CharSequence text) {
        TextView textView = (TextView) findViewById(id);
        setText(textView, text);
    }

    protected void setText(@IdRes int id, @StringRes int resId) {
        TextView textView = (TextView) findViewById(id);
        setText(textView, resId);
    }

    protected void setText(TextView textView, CharSequence text) {
        if (textView != null) {
            textView.setText(text);
        }
    }

    protected void setText(TextView textView, @StringRes int resId) {
        if (textView != null) {
            textView.setText(resId);
        }
    }

    protected void setImageResource(ImageView imageView, @DrawableRes int resId) {
        if (imageView != null) {
            imageView.setImageResource(resId);
        }
    }

    protected void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(@StringRes int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_LONG).show();
    }

    protected void setActionBar(String title, boolean isBack) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(isBack);
        }
    }

    protected void startActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // 返回按键处理
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
