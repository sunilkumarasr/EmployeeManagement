package com.royalitpark.employeemanagement.models

import com.google.gson.annotations.SerializedName

data class Leaves(
    @SerializedName("id"                ) var id             : String? = null,
    @SerializedName("emp_name"            ) var emp_name          : String? = null,
    @SerializedName("emp_id"            ) var empId          : String? = null,
    @SerializedName("is_half_day"       ) var isHalfDay      : String? = null,
    @SerializedName("emp_leave_type_id" ) var empLeaveTypeId : String? = null,
    @SerializedName("status_id"         ) var statusId       : String? = null,
    @SerializedName("from_date"         ) var fromDate       : String? = null,
    @SerializedName("to_date"           ) var toDate         : String? = null,
    @SerializedName("reason"            ) var reason         : String? = null,
    @SerializedName("leave_type"            ) var leave_type         : String? = null,
    @SerializedName("status_name"            ) var status_name         : String? = null,
    @SerializedName("requested_date"            ) var requested_date         : String? = null

)
