package com.sumair.dynamicform.auth.form.http.wrapper.form

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LayoutWrapper(@SerializedName("layout") @Expose val layout: ArrayList<Layout>) : Serializable {
}