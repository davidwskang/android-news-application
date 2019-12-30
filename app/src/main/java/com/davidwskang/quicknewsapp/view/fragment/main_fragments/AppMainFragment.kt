package com.davidwskang.quicknewsapp.view.fragment.main_fragments

import com.davidwskang.quicknewsapp.view.fragment.AppFragment

abstract class AppMainFragment : AppFragment() {

    abstract val fragmentIndex : Int

    abstract val actionBarTitle : String?

    abstract val actionBarIconId : Int

    abstract val actionBarSearchVisibility : Int
}
