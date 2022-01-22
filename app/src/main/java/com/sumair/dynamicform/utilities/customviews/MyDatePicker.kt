package com.sumair.dynamicform.utilities.customviews

import android.app.DatePickerDialog
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.sumair.dynamicform.auth.form.http.wrapper.form.ColData
import com.sumair.dynamicform.auth.form.http.wrapper.form.WidgetModel
import com.sumair.dynamicform.R
import java.util.*

class MyDatePicker {

    class Builder(val context: Context){
        lateinit var layout: ConstraintLayout
        lateinit var dialog: DatePickerDialog
        lateinit var dateTxt: TextView
        lateinit var calendar: Calendar
        var isClickable: Boolean = true

        fun init(parentLayout: LinearLayout, model: WidgetModel, values: ColData.WidgetValue) = apply{
            layout = LayoutInflater.from(context).inflate(R.layout.layout_datepicker, parentLayout, false) as ConstraintLayout
            dateTxt = layout.findViewById<TextView>(R.id.txt_selection)
            calendar = Calendar.getInstance()
            val dateListener = DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
                dateTxt.setText(year.toString() + "," + month + "," + dayOfMonth)
            }
            dialog = DatePickerDialog(context, dateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            if(model.props.name.contains(context.resources.getString(R.string.dob)))
                dialog.datePicker.maxDate = Date().time;
            layout.id = values.view_id
            layout.findViewById<TextView>(R.id.txt_label).setText(model.props.labelText + if(model.props.isRequired) context.getString(R.string.required_starric) else "")
            if(!TextUtils.isEmpty(values.value)) dateTxt.setText(values.value)

            layout.setOnClickListener{v ->
                dialog.show()
            }

            if(!isClickable){
                layout.isEnabled = false
            }

        }

        fun setClickableLayout(b: Boolean) = apply{
            isClickable = b
        }

        fun build() = layout
    }
}