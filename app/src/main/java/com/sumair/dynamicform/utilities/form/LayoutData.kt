package com.sumair.dynamicform.auth.form.http.wrapper.form

import java.io.Serializable

data class LayoutData( var columnModelList: ArrayList<ColumnModel> = ArrayList(),
                        var layoutId: Int = -1) : Serializable{

}