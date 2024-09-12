package com.royalitpark.employeemanagement.models

import com.google.gson.annotations.SerializedName

data class EmpDocuments(@SerializedName("id"            ) var id           : String? = null,
                        @SerializedName("emp_id"        ) var empId        : String? = null,
                        @SerializedName("display_name"  ) var displayName  : String? = null,
                        @SerializedName("document_name" ) var documentName : String? = null,
                        @SerializedName("document_file" ) var documentFile : String? = null,
                        @SerializedName("created_at"    ) var createdAt    : String? = null,
                        @SerializedName("updated_at"    ) var updatedAt    : String? = null)
