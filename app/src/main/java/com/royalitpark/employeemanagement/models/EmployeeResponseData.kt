package com.royalitpark.employeemanagement.models

import com.google.gson.annotations.SerializedName

data class EmployeeResponseData(
    @SerializedName("status"        ) var status       : Int?                    = null,
    @SerializedName("message"       ) var message      : String?                 = null,
    @SerializedName("employee_data" ) var employeeData : ArrayList<EmployeeData> = arrayListOf()
)
