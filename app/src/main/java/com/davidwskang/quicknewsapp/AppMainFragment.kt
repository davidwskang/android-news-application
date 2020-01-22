package com.davidwskang.quicknewsapp

import com.davidwskang.quicknewsapp.AppFragment

abstract class AppMainFragment : AppFragment() {

    abstract val fragmentIndex : Int

    abstract val actionBarTitle : String?

    abstract val actionBarIconId : Int

    abstract val actionBarSearchVisibility : Int
}
