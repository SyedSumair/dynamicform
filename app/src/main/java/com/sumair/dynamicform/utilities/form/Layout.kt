package com.sumair.dynamicform.auth.form.http.wrapper.form

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Layout(@SerializedName("title") @Expose val title: String,
                  @SerializedName("isDynamic") @Expose val isDynamic: Boolean,
                  @SerializedName("columns") @Expose val columns: ArrayList<ColumnModel>) : Serializable {

}




