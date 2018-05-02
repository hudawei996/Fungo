package com.fungo.repertory.image

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import android.widget.TextView
import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.baselib.base.recycler.BaseRecyclerAdapter
import com.fungo.baselib.base.recycler.BaseViewHolder
import com.fungo.repertory.R
import com.fungo.repertory.constant.IntentConstant
import kotlinx.android.synthetic.main.activity_image_main.*

/**
 * @author Pinger
 * @since 3/29/18 6:55 PM
 * 图片展示选项
 */
class ImageMainActivity : BaseActivity() {

    override val layoutResID: Int
        get() = R.layout.activity_image_main

    override fun initView() {
        setActionBar(getString(R.string.image_loader), true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ImageItemAdapter(this)
        recyclerView.adapter = adapter
        adapter.addAll(this.resources.getStringArray(R.array.image_action))
    }

    private class ImageItemAdapter(context: Context) : BaseRecyclerAdapter<String>(context) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
            return ImageItemHolder(parent)
        }

        class ImageItemHolder(parent: ViewGroup) : BaseViewHolder<String>(parent,
                android.R.layout.test_list_item) {
            override fun onBindData(data: String) {

                val textView = findView<TextView>(android.R.id.text1)
                textView.setPadding(36, 24, 0, 24)
                textView.setTextColor(Color.DKGRAY)
                textView.textSize = 24f
                setText(android.R.id.text1, data)
            }

            override fun onItemClick(data: String) {
                val intent = Intent(context, ImageItemActivity::class.java)
                intent.putExtra(IntentConstant.IMAGE_ITEM_POSITION, dataPosition)
                context.startActivity(intent)
            }
        }
    }


}