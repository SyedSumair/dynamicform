package com.sumair.dynamicform.auth.form.http.wrapper.form

import java.io.Serializable

data class MyColumnModelData(var columnDataList: ArrayList<ColData>?,
                        var columnData: ColData?) : Serializable {
}