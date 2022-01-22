package com.sumair.dynamicform.auth.form.http.wrapper.form

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WidgetModel(@SerializedName("id") @Expose val id: String,
                  @SerializedName("form") @Expose val form: String,
                  @SerializedName("name") @Expose val name: String,
                  @SerializedName("type") @Expose val type: String,
                  @SerializedName("props") @Expose val props: Props,
                  @SerializedName("parentId") @Expose val parentId: String,
                  @SerializedName("component") @Expose val component: String) : Serializable {

    data class Props(@SerializedName("name") @Expose val name: String,
                     @SerializedName("field") @Expose val field: String,
                     @SerializedName("label") @Expose val label: String,
                     @SerializedName("labelText") @Expose val labelText: String,
                     @SerializedName("placeholderText") @Expose val placeholderText: String,
                     @SerializedName("options") @Expose val options: Map<String, String>,
                     @SerializedName("isMultiple") @Expose val isMultiple: Boolean,
                     @SerializedName("isRequired") @Expose var isRequired: Boolean,
                     @SerializedName("fileSize") @Expose val fileSize: Int,
                     @SerializedName("fileType") @Expose val fileType: String,
                     @SerializedName("fileLimit") @Expose val fileLimit: Int,
                     @SerializedName("attachmentLabel") @Expose val attachmentLabel: String) : Serializable {

    }
}
