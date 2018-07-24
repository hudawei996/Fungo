package com.fungo.repertory.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fungo.baselib.base.basic.BaseFragment
import com.fungo.repertory.R

/**
 * @author Pinger
 * @since 18-7-24 下午1:09
 *
 */

class MainFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main,container,false)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }


}