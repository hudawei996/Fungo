package com.fungo.baselib.base.recycler

import com.fungo.baselib.base.mvp.IBasePresenter
import com.fungo.baselib.base.mvp.IBaseView

/**
 * @author Pinger
 * @since 18-7-13 下午3:09
 * 列表Fragment MVP协议
 */

class BaseRecyclerContract {


    interface View<in T> : IBaseView {

        /**
         * 展示第一屏内容
         * @param datas 加载第一屏的数据
         */
        fun showContent(page: Int, datas: List<T>)

        /**
         * 更新某一条数据
         */
        fun updateItem(data: T, position: Int)


        /**
         * 结束加载状态
         * @param isRefresh 是否是下拉刷新操作
         */
        fun finishLoading(isRefresh: Boolean)

    }


    interface Presenter : IBasePresenter {

        /**
         * 加载数据
         */
        fun loadData(page: Int)
    }

}