package com.fungo.repertory.main

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.fungo.baselib.base.page.BasePageFragment
import com.fungo.repertory.R
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * @author Pinger
 * @since 18-7-24 下午1:09
 *
 */

class MainFragment : BasePageFragment() {

    override fun getLayoutResId(): Int {
        return R.layout.fragment_main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        button.setOnClickListener {
            Navigation.findNavController(getView()!!).navigate(R.id.fragment_next)
        }
    }

}