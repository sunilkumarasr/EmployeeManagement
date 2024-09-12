package com.royalitpark.employeemanagement.models

import com.google.gson.annotations.SerializedName


data class AttendanceModel(
    @SerializedName("status"       ) var status      : Int?                   = null,
    @SerializedName("message"      ) var message     : String?                = "",
    @SerializedName("emp_date_of_joining"      ) var emp_date_of_joining     : String?                = "",
    @SerializedName("present_days_count"      ) var present_days_count     : String?                = "",
    @SerializedName("absent_days_count"      ) var absent_days_count     : String?                = "",
    @SerializedName("half_days_count"      ) var half_days_count     : String?                = "",
    @SerializedName("half_day_leaves_count"      ) var half_day_leaves_count     : String?                = "",
    @SerializedName("full_day_leaves_count"      ) var full_day_leaves_count     : String?                = "",
    @SerializedName("holidays_count"      ) var holidays_count     : String?                = "",
    @SerializedName("leaves"      ) var leaves     : String?                = "",
    @SerializedName("levaes_list"       ) var levaes_list      : ArrayList<LeavesAttend>      = arrayListOf(),
    @SerializedName("holidays"     ) var holidays    : ArrayList<Holidays>    = arrayListOf(),
    @SerializedName("working_days" ) var workingDays : ArrayList<WorkingDays> = arrayListOf(),
    @SerializedName("weekoffs" ) var weekoffs : ArrayList<String> = arrayListOf()
)

data class CalanderModelMainRespon(
    @SerializedName("status"               ) var status            : Int?            = null,
    @SerializedName("message"              ) var message           : String?         = null,
    @SerializedName("data"                 ) var data              : ArrayList<CalanderModel> = arrayListOf(),
    @SerializedName("absent_count"         ) var absentCount       : Int?            = null,
    @SerializedName("full_day_leave_count" ) var fullDayLeaveCount : Int?            = null,
    @SerializedName("half_day_leave_count" ) var halfDayLeaveCount : Int?            = null,
    @SerializedName("week_off_count"       ) var weekOffCount      : Int?            = null,
    @SerializedName("present_day_count"    ) var presentDayCount   : Int?            = null,
    @SerializedName("emp_date_of_joining"    ) var emp_date_of_joining   : String?            = null
)

data class CalanderModel(
    @SerializedName("date"   ) var date   : String? = null,
    @SerializedName("status" ) var status : String? = null,
    @SerializedName("punch_in" ) var punch_in : String = "",
    @SerializedName("punch_out" ) var punch_out : String = ""
)