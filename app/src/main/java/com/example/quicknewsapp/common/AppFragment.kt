package com.example.quicknewsapp.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

abstract class AppFragment : Fragment() {

    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    var mainActivity : MainActivity? = null
        get() {return activity as MainActivity?}


    abstract val layoutId : Int
}