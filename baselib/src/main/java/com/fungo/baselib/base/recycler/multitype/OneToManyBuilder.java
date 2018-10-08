package com.fungo.baselib.base.recycler.multitype;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import static com.fungo.baselib.base.recycler.multitype.Preconditions.checkNotNull;


class OneToManyBuilder<T> implements OneToManyFlow<T>, OneToManyEndpoint<T> {

    private final @NonNull
    MultiTypeAdapter adapter;
    private final @NonNull
    Class<? extends T> clazz;
    private MultiTypeViewHolder<T, ?>[] binders;


    OneToManyBuilder(@NonNull MultiTypeAdapter adapter, @NonNull Class<? extends T> clazz) {
        this.clazz = clazz;
        this.adapter = adapter;
    }


    @Override
    @CheckResult
    @SafeVarargs
    public final @NonNull
    OneToManyEndpoint<T> to(@NonNull MultiTypeViewHolder<T, ?>... binders) {
        checkNotNull(binders);
        this.binders = binders;
        return this;
    }


    @Override
    public void withLinker(@NonNull Linker<T> linker) {
        checkNotNull(linker);
        doRegister(linker);
    }


    @Override
    public void withClassLinker(@NonNull ClassLinker<T> classLinker) {
        checkNotNull(classLinker);
        doRegister(ClassLinkerWrapper.wrap(classLinker, binders));
    }


    private void doRegister(@NonNull Linker<T> linker) {
        for (MultiTypeViewHolder<T, ?> binder : binders) {
            adapter.register(clazz, binder, linker);
        }
    }
}
