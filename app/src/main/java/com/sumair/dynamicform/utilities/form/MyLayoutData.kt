package com.sumair.dynamicform.auth.form.http.wrapper.form

import java.io.Serializable

data class MyLayoutData(var layoutDataId: Int?,
        var columnModelData: ArrayList<MyColumnModelData>) : Serializable{
}