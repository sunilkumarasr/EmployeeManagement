package com.royalitpark.employeemanagement.models

import com.google.gson.annotations.SerializedName

data class LeaveTypes (

    @SerializedName("id"         ) var id        : String? = null,
    @SerializedName("leave_type" ) var leaveType : String? = null

)