package com.fungo.sample.main

import android.os.Handler
import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.baselib.base.basic.BaseFragment
import com.fungo.sample.R
import com.fungo.sample.netgo.NetGoFragment

/**
 * @author Pinger
 * @since 18-7-24 下午2:56
 * 填充一个主页
 */

class MainActivity(override val layoutResID: Int = R.layout.activity_main) : BaseActivity() {


//    override fun getPageFragment(): BasePageFragment {
//        return MainFragment()
//    }
//
//    override fun isSwipeBackEnable(): Boolean = false


    override fun initView() {


        Handler().postDelayed({

            val fragments = arrayListOf<BaseFragment>()

            fragments.add(NetGoFragment())
            fragments.add(NetGoFragment())
            fragments.add(NetGoFragment())
            fragments.add(NetGoFragment())


            supportDelegate.loadMultipleRootFragment(R.id.mainContainer, 0,
                    fragments[0],
                    fragments[1],
                    fragments[2],
                    fragments[3]
            )
        },3000)



    }


}
