package com.example.quicknewsapp.secondary_fragments.splash

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import com.example.quicknewsapp.common.AppFragment
import com.example.quicknewsapp.R
import com.example.quicknewsapp.api.LocationApi
import com.example.quicknewsapp.api.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_splash.*
import timber.log.Timber

/***
 * This fragment allows the app to initialize required data.
 * 1. User location
 * 2. Sources from NewsAPI
 */
class SplashScreenFragment : AppFragment() {

    override val layoutId = R.layout.fragment_splash

    var locationReceived = false

    companion object {
        fun newInstance(): SplashScreenFragment {
            val fragment = SplashScreenFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottom_drawer_menu_container.setOnTouchListener { _, _ -> true }

        mainActivity!!.API_KEY = resources.getString(R.string.API_KEY)

        Resources.getSystem().displayMetrics.run {
            mainActivity!!.screenWidth = heightPixels
            mainActivity!!.screenHeight = widthPixels
        }
        getUserLocation()
    }

    private fun getUserLocation() {
        val obs = RetrofitClient.getLocationInstance()
                .create(LocationApi::class.java)
                .getLocation()
        val observer = obs.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe({
                    mainActivity!!.onLocationReceived(location = it)
                    locationReceived = true
                    if (isAllLoaded()) {
                        mainActivity!!.destroySplashScreen()
                    }
                }, {
                    Timber.e(it)
                })
        compositeDisposable.add(observer)
    }

    private fun isAllLoaded() = locationReceived
}