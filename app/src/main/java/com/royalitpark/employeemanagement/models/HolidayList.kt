package com.royalitpark.employeemanagement.models

import com.google.gson.annotations.SerializedName

data class HolidayList (

    @SerializedName("id"     ) var id     : String? = null,
    @SerializedName("date"   ) var date   : String? = null,
    @SerializedName("reason" ) var reason : String? = null

)