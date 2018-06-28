package com.fungo.baseuilib.view.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author Pinger
 * @since 18-6-11 下午9:10
 */

public class ImageWrapper extends AppCompatImageView {

    public ImageWrapper(Context context) {
        super(context);
    }

    public ImageWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isNeedFilter()) {
                    Drawable drawable = getDrawable();
                    if (drawable != null) {
                        /**
                         * 默认情况下，所有的从同一资源（R.drawable.XXX）加载来的drawable实例都共享一个共用的状态，
                         * 如果你更改一个实例的状态，其他所有的实例都会收到相同的通知。
                         * 使用使 mutate 可以让这个drawable变得状态不定。这个操作不能还原（变为不定后就不能变为原来的状态）。
                         * 一个状态不定的drawable可以保证它不与其他任何一个drawabe共享它的状态。
                         * 此处应该是要使用的 mutate()，但是在部分手机上会出现点击后变白的现象，所以没有使用
                         * 目前这种解决方案没有问题，这种方式需要ImageView设置点击事件
                         */
                        drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                        ViewCompat.postInvalidateOnAnimation(this);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isNeedFilter()) {
                    Drawable drawableUp = getDrawable();
                    if (drawableUp != null) {
                        drawableUp.clearColorFilter();
                        ViewCompat.postInvalidateOnAnimation(this);
                    }
                }
                break;
        }

        return super.onTouchEvent(event);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);
    }

    public boolean isNeedFilter() {
        return true;
    }

}
