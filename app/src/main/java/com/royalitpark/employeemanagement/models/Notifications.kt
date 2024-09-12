package com.royalitpark.employeemanagement.models

import com.google.gson.annotations.SerializedName

data class Notifications(
    @SerializedName("id"           ) var id          : String? = null,
    @SerializedName("emp_id"       ) var empId       : String? = null,
    @SerializedName("title"        ) var title       : String? = null,
    @SerializedName("body"         ) var body        : String? = null,
    @SerializedName("created_at" ) var created_at : String? = null
)