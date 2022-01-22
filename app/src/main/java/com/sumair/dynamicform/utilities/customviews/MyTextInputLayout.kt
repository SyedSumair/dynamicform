package com.sumair.dynamicform.utilities.customviews

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputLayout
import com.sumair.dynamicform.R
import com.sumair.dynamicform.auth.form.http.wrapper.form.ColData
import com.sumair.dynamicform.auth.form.http.wrapper.form.WidgetModel

class MyTextInputLayout {

    class Builder(val context: Context) {
        lateinit var layout: LinearLayout
        private lateinit var editText: EditText
        var isClickable: Boolean = true

        fun init(parentLayout: LinearLayout, model: WidgetModel, values: ColData.WidgetValue) = apply {
            layout = LayoutInflater.from(context).inflate(R.layout.layout_text_field, parentLayout, false) as LinearLayout
            editText = layout.findViewById(R.id.et_textField)

            var inputType: Int = InputType.TYPE_CLASS_TEXT
            if (model.component.equals("email")) {
                inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            } else if (model.component.equals("custom")) inputType = InputType.TYPE_CLASS_TEXT

            editText.inputType = inputType


            layout.findViewById<TextInputLayout>(R.id.textinputLayout).hint = model.props.labelText + if (model.props.isRequired) context.getString(R.string.required_starric) else ""
            layout.id = values.view_id
            if (!TextUtils.isEmpty(values.value)) {
                editText.setText(values.value)
                if (model.props.name.contains("email") && values.isPrefilled) {
                    editText.isEnabled = false
                    values.makeParam = false
                }
            }

            if (!isClickable) {
                editText.isEnabled = false
            }
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    layout.findViewById<TextInputLayout>(R.id.textinputLayout).error = null
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }

        fun setClickableLayout(b: Boolean) = apply {
            isClickable = b
        }

        fun build() = layout
    }
}