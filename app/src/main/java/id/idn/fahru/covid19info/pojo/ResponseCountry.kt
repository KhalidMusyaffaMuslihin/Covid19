package id.idn.fahru.covid19info.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Imam Fahrur Rofi on 29/07/2020.
 */

@Parcelize
data class ResponseCountry(
    @field:SerializedName("Country")
    val Country: String?,
    @field:SerializedName("CountryCode")
    val CountryCode: String?,
    @field:SerializedName("Province")
    val Province: String?,
    @field:SerializedName("City")
    val City: String?,
    @field:SerializedName("Lat")
    val Lat: String?,
    @field:SerializedName("Lon")
    val Lon: String?,
    @field:SerializedName("Confirmed")
    val Confirmed: Int?,
    @field:SerializedName("Deaths")
    val Deaths: Int?,
    @field:SerializedName("Recovered")
    val Recovered: Int?,
    @field:SerializedName("Active")
    val Active: Int?,
    @field:SerializedName("Date")
    val Date: String?
) : Parcelable
