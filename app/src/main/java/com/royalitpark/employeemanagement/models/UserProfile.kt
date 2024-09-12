package com.royalitpark.employeemanagement.models

import com.google.gson.annotations.SerializedName

data class UserProfile (
    @SerializedName("id"            ) var id           : String? = null,
    @SerializedName("status_id"     ) var statusId     : String? = null,
    @SerializedName("shift_id"      ) var shiftId      : String? = null,
    @SerializedName("is_deleted"    ) var isDeleted    : String? = null,
    @SerializedName("login_otp"     ) var loginOtp     : String? = null,
    @SerializedName("full_name"     ) var fullName     : String? = null,
    @SerializedName("email"         ) var email        : String? = null,
    @SerializedName("mobile"        ) var mobile       : String? = null,
    @SerializedName("gender"        ) var gender       : String? = null,
    @SerializedName("dob"           ) var dob          : String? = null,
    @SerializedName("address"       ) var address      : String? = null,
    @SerializedName("aadhar_number" ) var aadharNumber : String? = null,
    @SerializedName("username"      ) var username     : String? = null,
    @SerializedName("password"      ) var password     : String? = null,
    @SerializedName("created_at"    ) var createdAt    : String? = null,
    @SerializedName("profile_image"    ) var profile_image    : String? = null,
    @SerializedName("updated_at"    ) var updatedAt    : String? = null,
    @SerializedName("designation"    ) var designation    : String? = null,
    @SerializedName("joining_date"    ) var joining_date    : String? = null,
    @SerializedName("company_name"    ) var company_name    : String? = null
){}