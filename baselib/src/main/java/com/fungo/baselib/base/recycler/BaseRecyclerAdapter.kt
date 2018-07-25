package com.fungo.baselib.base.recycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import java.util.*

/**
 * @author pinger
 * @since 2018/1/13 23:53
 * 基类适配器封装，将数据和UI抽离到具体的Holder中实现，适配器只关心数据状态
 */
abstract class BaseRecyclerAdapter<T> : RecyclerView.Adapter<BaseViewHolder<T>> {

    private lateinit var mDatas: ArrayList<T>
    private lateinit var mObserver: RecyclerView.AdapterDataObserver
    private lateinit var mContext: Context
    private var mNotifyOnChange = true
    private val mLock = Any()

    /**
     * Constructor
     *
     * @param context The current context.
     */
    constructor(context: Context) {
        init(context, ArrayList())
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param datas The objects to represent in the ListView.
     */
    constructor(context: Context, datas: Array<T>) {
        init(context, datas.toList())
    }


    /**
     * Constructor
     *
     * @param context The current context.
     * @param datas The objects to represent in the ListView.
     */
    constructor(context: Context, datas: List<T>) {
        init(context, datas)
    }

    private fun init(context: Context, datas: List<T>) {
        mContext = context
        mDatas = ArrayList<T>(datas)
    }

    fun getCount(): Int {
        return mDatas.size
    }


    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.onBindData(getItemData(position))
    }

    fun getAllData(): List<T> {
        return ArrayList<T>(mDatas)
    }

    fun getItemData(position: Int): T {
        return mDatas[position]
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item.
     */
    fun getPosition(item: T): Int {
        return mDatas.indexOf(item)
    }


    /**
     * Returns the context associated with this array adapter. The context is used
     * to create views from the resource passed to the constructor.
     *
     * @return The Context associated with this adapter.
     */
    fun getContext(): Context {
        return mContext
    }

    fun setContext(context: Context) {
        mContext = context
    }

    /**
     * Control whether methods that change the list ([.add],
     * [.insert], [.remove], [.clear]) automatically call
     * [.notifyDataSetChanged].  If set to false, caller must
     * manually call notifyDataSetChanged() to have the changes
     * reflected in the attached view.
     *
     *
     * The default is true, and calling notifyDataSetChanged()
     * resets the flag to true.
     *
     * @param notifyOnChange if true, modifications to the list will
     * automatically call {@link #notifyDataSetChanged}
     */
    fun setNotifyOnChange(notifyOnChange: Boolean) {
        mNotifyOnChange = notifyOnChange
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param data The object to add at the end of the array.
     */
    fun add(data: T) {
        if (data != null) {
            synchronized(mLock) {
                mDatas.add(data)
            }
        }
        mObserver.onItemRangeInserted(getCount(), 1)
        if (mNotifyOnChange) notifyItemInserted(getCount())
    }


    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     */
    fun addAll(collection: Collection<T>?) {
        if (collection != null && collection.isNotEmpty()) {
            synchronized(mLock) {
                mDatas.addAll(collection)
            }
        }
        val dataCount = collection?.size ?: 0
        //mObserver.onItemRangeInserted(getCount() - dataCount, dataCount)
        if (mNotifyOnChange) notifyItemRangeInserted(getCount() - dataCount, dataCount)

    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param datas The items to add at the end of the array.
     */
    fun addAll(datas: Array<T>?) {
        if (datas != null && datas.isNotEmpty()) {
            synchronized(mLock) {
                mDatas.addAll(datas.toList())
            }
        }
        val dataCount = datas?.size ?: 0
        //mObserver.onItemRangeInserted(getCount() - dataCount, dataCount)
        if (mNotifyOnChange) notifyItemRangeInserted(getCount() - dataCount, dataCount)
    }

    /**
     * set the specified Collection at the data.
     *
     * @param collection The Collection to add data.
     */
    fun setData(collection: Collection<T>?) {
        if (collection != null && collection.isNotEmpty()) {
            synchronized(mLock) {
                clear()
                mDatas.addAll(collection)
            }
        }
        val dataCount = collection?.size ?: 0
        mObserver.onItemRangeInserted(0, dataCount)
        if (mNotifyOnChange) notifyItemRangeInserted(0, dataCount)
    }


    /**
     * @param datas The object to insert into the array.
     * @param index  The index at which the object must be inserted.
     */
    fun insert(datas: T, index: Int) {
        synchronized(mLock) {
            mDatas.add(index, datas)
        }
        mObserver.onItemRangeInserted(index, 1)
        if (mNotifyOnChange) notifyItemInserted(index)
    }

    /**
     * @param datas The object to insert into the array.
     * @param index  The index at which the object must be inserted.
     */
    fun insertAll(datas: Array<T>?, index: Int) {
        synchronized(mLock) {
            mDatas.addAll(index, datas?.toList()!!)
        }
        val dataCount = datas?.size ?: 0
        mObserver.onItemRangeInserted(index, dataCount)
        if (mNotifyOnChange) notifyItemRangeInserted(index, dataCount)
    }


    fun insertAll(datas: Collection<T>?, index: Int) {
        synchronized(mLock) {
            mDatas.addAll(index, datas!!)
        }
        val dataCount = datas?.size ?: 0
        mObserver.onItemRangeInserted(index, dataCount)
        if (mNotifyOnChange) notifyItemRangeInserted(index, dataCount)
    }

    fun update(datas: T, pos: Int) {
        synchronized(mLock) {
            mDatas.set(pos, datas)
        }
        mObserver.onItemRangeChanged(pos, 1)
        if (mNotifyOnChange) notifyItemChanged(pos)
    }

    /**
     * @param datas The object to remove.
     */
    fun remove(datas: T) {
        val position = mDatas.indexOf(datas)
        synchronized(mLock) {
            if (mDatas.remove(datas)) {
                mObserver.onItemRangeRemoved(position, 1)
                if (mNotifyOnChange) notifyItemRemoved(position)
            }
        }
    }

    /**
     * @param position The position of the object to remove.
     */
    fun remove(position: Int) {
        synchronized(mLock) {
            mDatas.removeAt(position)
        }
        mObserver.onItemRangeRemoved(position, 1)
        if (mNotifyOnChange) notifyItemRemoved(position)
    }

    /**
     * 触发清空
     * 与[.clear]的不同仅在于这个使用notifyItemRangeRemoved.
     * 猜测这个方法与add伪并发执行的时候会造成"Scrapped or attached views may not be recycled"的Crash.
     * 所以建议使用[.clear]
     */
    fun removeAll() {
        val count = mDatas.size
        synchronized(mLock) {
            mDatas.clear()
        }
        mObserver.onItemRangeRemoved(0, count)
        if (mNotifyOnChange) notifyItemRangeRemoved(0, count)
    }


    fun clear() {
        synchronized(mLock) {
            mDatas.clear()
        }
        mObserver.onChanged()
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained
     * in this adapter.
     */
    fun sort(comparator: Comparator<in T>) {
        synchronized(mLock) {
            Collections.sort(mDatas, comparator)
        }
        if (mNotifyOnChange) notifyDataSetChanged()
    }
}