package com.royalitpark.employeemanagement.models

import com.google.gson.annotations.SerializedName

data class WorkingDays(
    @SerializedName("id"                ) var id             : String? = null,
    @SerializedName("emp_id"            ) var empId          : String? = null,
    @SerializedName("date"       ) var date      : String? = null,
    @SerializedName("punch_in" ) var punchin : String? = null,
    @SerializedName("punch_out"         ) var punchout       : String? = null,
    @SerializedName("punch_in_image"         ) var punch_in_image       : String? = null,
    @SerializedName("is_half_day"       ) var isHalfDay      : String? = null,
)
