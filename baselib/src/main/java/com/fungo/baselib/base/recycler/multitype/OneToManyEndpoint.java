package com.fungo.baselib.base.recycler.multitype;

import android.support.annotation.NonNull;

public interface OneToManyEndpoint<T> {

    void withLinker(@NonNull Linker<T> linker);

    void withClassLinker(@NonNull ClassLinker<T> classLinker);
}