package com.sumair.dynamicform.auth.form.http.wrapper.form

import com.sumair.dynamicform.utilities.NumberUtils
import java.io.Serializable


data class ColData(var columnDataId: Int?,
                    var columnId: Int = -1,
                    var title: String = "",
                   var isDynamic: Boolean,
                   var parentId: String,
                   var widgets: ArrayList<WidgetModel>,
                   var widgetsValue: ArrayList<WidgetValue>) : Serializable {

    init {
        this.widgetsValue = loadWidgetValues(widgets)
    }
    fun loadWidgetValues(widgets: ArrayList<WidgetModel>) : ArrayList<WidgetValue>{
        val list : ArrayList<WidgetValue> = ArrayList()
        for (widget in widgets){
            list.add(WidgetValue())
        }
        return list
    }

    data class WidgetValue(var view_id: Int = -1,
                           var type: String = "",
                           var name: String = "",
                           var value: String = "",
                           var isPrefilled: Boolean = false,
                           var makeParam: Boolean = true,
                           var selectedIndex: ArrayList<Int> = ArrayList(),
                           var profileImage: String = "",
                           var attachments: ArrayList<String> =  ArrayList(),
                           var attachmentNames: ArrayList<String> =  ArrayList()) : Serializable {

        init {
            this.view_id = NumberUtils.getNextNumber()
        }
    }

}