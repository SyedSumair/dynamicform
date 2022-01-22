package com.sumair.dynamicform.utilities.customviews

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.sumair.dynamicform.auth.form.http.wrapper.form.ColData
import com.sumair.dynamicform.auth.form.http.wrapper.form.WidgetModel
import com.sumair.dynamicform.R


class MyTextArea {

    class Builder(val context: Context){
        lateinit var layout: ConstraintLayout
        private lateinit var editText: EditText
        var isClickable: Boolean = true

        fun init(parentLayout: LinearLayout, model: WidgetModel, values: ColData.WidgetValue) = apply {
            layout = LayoutInflater.from(context).inflate(R.layout.layout_text_area, parentLayout, false) as ConstraintLayout
            editText = layout.findViewById(R.id.et_multiline)

            /*var inputType: Int = InputType.TYPE_CLASS_TEXT
            if(model.component.equals("email")) inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            else if (model.component.equals("custom")) inputType = InputType.TYPE_CLASS_TEXT
            editText.inputType = inputType
*/
            layout.id = values.view_id

            editText.setHint(model.props.placeholderText)
            layout.findViewById<TextView>(R.id.txt_label).setText(model.props.labelText + if(model.props.isRequired) context.getString(R.string.required_starric) else "")
            if(!TextUtils.isEmpty(values.value)) editText.setText(values.value)

            editText.setOnTouchListener { v, event ->
                if (editText.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true)
                    when (event.action and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_SCROLL -> {
                            v.getParent().requestDisallowInterceptTouchEvent(false)
                            return@setOnTouchListener true
                        }
                    }
                }
                return@setOnTouchListener false
            }

            if(!isClickable){
                editText.isEnabled = false
            }

        }

        fun setClickableLayout(b: Boolean) = apply{
            isClickable = b
        }


        fun build() = layout
    }
}