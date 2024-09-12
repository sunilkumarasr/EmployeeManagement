package com.royalitpark.employeemanagement.models

import com.google.gson.annotations.SerializedName

data class EmployeeShiftDetailsResponse(
    @SerializedName("status"                ) var status              : Int?                           = null,
    @SerializedName("message"               ) var message             : String?                        = null,
    @SerializedName("emp_details"           ) var empDetails          : ArrayList<EmployeeData>          = arrayListOf(),
    @SerializedName("shift_details"         ) var shiftDetails        : ArrayList<ShiftDetails>        = arrayListOf(),
    @SerializedName("assigned_working_days" ) var assignedWorkingDays : ArrayList<AssignedWorkingDays> = arrayListOf()
)
