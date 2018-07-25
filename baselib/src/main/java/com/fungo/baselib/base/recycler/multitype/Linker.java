package com.fungo.baselib.base.recycler.multitype;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

public interface Linker<T> {
    @IntRange(from = 0)
    int index(int position, @NonNull T t);
}