package com.fungo.baselib.base.recycler.multitype;

import android.support.annotation.NonNull;

public interface ClassLinker<T> {
    @NonNull
    Class<? extends MultiTypeViewHolder<T, ?>> index(int position, @NonNull T t);
}
