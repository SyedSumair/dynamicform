package com.sumair.dynamicform.auth.form.http.wrapper.form

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class ColumnModel(@SerializedName("title") @Expose val title: String,
                       @SerializedName("isDynamic") @Expose val isDynamic: Boolean,
                       @SerializedName("parentId") @Expose val parentId: String,
                       @SerializedName("widgets") @Expose val widgets: ArrayList<WidgetModel>
) : Serializable {

}
