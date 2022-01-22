package com.sumair.dynamicform.auth.form.http.wrapper.form

import java.io.Serializable

data class MyLayoutModel(var layoutDataList: ArrayList<MyLayoutData>?,
                        var layoutData: MyLayoutData?) : Serializable {
}