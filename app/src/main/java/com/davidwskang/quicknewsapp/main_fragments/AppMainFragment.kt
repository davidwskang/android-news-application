package com.davidwskang.quicknewsapp.main_fragments

import com.davidwskang.quicknewsapp.common.AppFragment

abstract class AppMainFragment : AppFragment() {

    abstract val fragmentIndex : Int

    abstract val actionBarTitle : String?

    abstract val actionBarIconId : Int

    abstract val actionBarSearchVisibility : Int
}
