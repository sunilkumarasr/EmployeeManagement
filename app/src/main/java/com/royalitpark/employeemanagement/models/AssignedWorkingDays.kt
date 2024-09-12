package com.royalitpark.employeemanagement.models

import com.google.gson.annotations.SerializedName

data class AssignedWorkingDays(@SerializedName("id"       ) var id      : String? = null,
                               @SerializedName("day_name" ) var dayName : String? = null)
