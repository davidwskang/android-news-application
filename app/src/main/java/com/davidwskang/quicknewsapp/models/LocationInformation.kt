package com.davidwskang.quicknewsapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class LocationInformation(
        var status : String,
        var country : String,
        var countryCode : String
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        other as LocationInformation
        return countryCode.toLowerCase(Locale.ROOT) == other.countryCode.toLowerCase(Locale.ROOT)
    }

    override fun hashCode(): Int {
        var result = status.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + countryCode.hashCode()
        return result
    }
}