package com.royalitpark.employeemanagement.models

import com.google.gson.annotations.SerializedName

data class BankDetails (

    @SerializedName("id"                  ) var id                : String? = null,
    @SerializedName("employee_id"         ) var employeeId        : String? = null,
    @SerializedName("bank_name"           ) var bankName          : String? = null,
    @SerializedName("account_number"      ) var accountNumber     : String? = null,
    @SerializedName("ifsc_code"           ) var ifscCode          : String? = null,
    @SerializedName("account_holder_name" ) var accountHolderName : String? = null,
    @SerializedName("created_at"          ) var createdAt         : String? = null,
    @SerializedName("updated_at"          ) var updatedAt         : String? = null

)