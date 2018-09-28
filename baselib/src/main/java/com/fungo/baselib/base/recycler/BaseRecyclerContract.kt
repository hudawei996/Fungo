package com.fungo.baselib.base.recycler

import com.fungo.baselib.base.mvp.IBasePresenter
import com.fungo.baselib.base.mvp.IBaseView

/**
 * @author Pinger
 * @since 18-7-13 下午3:09
 * 列表Fragment MVP协议
 */

class BaseRecyclerContract {


    interface View: IBaseView {


        /**
         * 往集合底端添加一条数据
         */
        fun <T> addItem(data: T)

        /**
         * 指定某一个位置插入一条数据
         */
        fun  <T> insertItem(position: Int, data: T)

        /**
         * 展示第一屏内容
         * @param datas 加载第一屏的数据
         */
        fun  <T> showContent(page: Int, datas: List<T>)

        /**
         * 更新某一条数据
         */
        fun  <T> updateItem(data: T, position: Int)
    }


    interface Presenter : IBasePresenter {

        /**
         * 加载数据
         */
        fun loadData(page: Int)

        /**
         * 接口里重载
         */
        override fun onStart() {

        }

        override fun onStop() {

        }
    }

}