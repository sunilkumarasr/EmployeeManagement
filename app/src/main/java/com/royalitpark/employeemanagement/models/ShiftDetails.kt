package com.royalitpark.employeemanagement.models

import com.google.gson.annotations.SerializedName

data class ShiftDetails(@SerializedName("id"         ) var id        : String? = null,
                        @SerializedName("start"      ) var start     : String? = null,
                        @SerializedName("end"        ) var end       : String? = null,
                        @SerializedName("created_at" ) var createdAt : String? = null,
                        @SerializedName("updated_at" ) var updatedAt : String? = null)
