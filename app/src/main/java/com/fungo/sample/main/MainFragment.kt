package com.fungo.sample.main

import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.fungo.baseuilib.recycler.BaseRecyclerContract
import com.fungo.baseuilib.recycler.BaseRecyclerFragment
import com.fungo.baseuilib.theme.UiUtils
import com.fungo.sample.R
import kotlinx.android.synthetic.main.fragment_main.*


/**
 * @author Pinger
 * @since 18-10-15 下午6:33
 * 主页，存放各个类库Demo的入口
 */

class MainFragment : BaseRecyclerFragment() {

    override fun getContentResID(): Int {
        return  R.layout.fragment_main
    }

    override fun getPageTitle(): String? {
        return getString(R.string.app_name)
    }

    override fun initPageView() {
        setSmartRefreshLayout(smartRefreshLayout)
        setRecyclerView(recyclerView)
        register(MainBean::class.java, MainHolder())
        UiUtils.setAddIconFont(floatActionButton)

        floatActionButton.setOnClickListener {
            showColorPiker()
        }
    }


    /**
     * 主题颜色选择器
     */
    private fun showColorPiker() {
        // 设置主题
//        ColorChooserDialog.Builder(getPageActivity()!!, R.string.theme_choose)
//                .customColors(R.array.colors, null)
//                .doneButton(R.string.confirm)
//                .cancelButton(R.string.cancel)
//                .allowUserColorInput(false)
//                .allowUserColorInputAlpha(false)
//                .show()


        MaterialDialog(context!!)
                .title(R.string.theme_choose)
                .colorChooser(context!!.resources.getIntArray(R.array.colors), initialSelection = UiUtils.getCurrentThemeColor(context!!)) { dialog, color ->
                  context!!.setTheme(color)
                }
                .positiveButton(R.string.confirm)
                .show()
    }

    override fun getPresenter(): BaseRecyclerContract.Presenter {
        return MainPresenter(this)
    }

    override fun isShowBackIcon(): Boolean = false

    override fun isSwipeBackEnable(): Boolean = false


    override fun isEnablePureScrollMode(): Boolean = true
}