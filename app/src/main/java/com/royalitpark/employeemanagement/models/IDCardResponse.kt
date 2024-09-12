package com.royalitpark.employeemanagement.models

import com.google.gson.annotations.SerializedName

data class IDCardResponse(@SerializedName("status"  ) var status  : Int?    = null,
                          @SerializedName("message" ) var message : String? = null,
                          @SerializedName("id_card" ) var idCard  : String? = null,
                          @SerializedName("id_card_full_path" ) var id_card_full_path  : String? = null)
