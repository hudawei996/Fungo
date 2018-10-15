package com.fungo.baselib.base.recycler.multitype;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

public interface OneToManyFlow<T> {

    @CheckResult
    @SuppressWarnings("unchecked")
    @NonNull
    OneToManyEndpoint<T> to(@NonNull MultiTypeViewHolder<T, ?>... binders);
}