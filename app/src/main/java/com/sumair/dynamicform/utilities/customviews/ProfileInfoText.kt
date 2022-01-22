package com.sumair.dynamicform.utilities.customviews

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.sumair.dynamicform.R
import com.sumair.dynamicform.auth.form.http.wrapper.form.ColData
import com.sumair.dynamicform.auth.form.http.wrapper.form.WidgetModel
import com.sumair.dynamicform.utilities.constants.FormConstants
import com.sumair.dynamicform.utilities.wrappers.LanguageWrapper

class ProfileInfoText {

    class Builder(val context: Context) {
        lateinit var layout: ConstraintLayout
        private lateinit var txtTitle: TextView
        private lateinit var txtlabel: TextView
        private lateinit var languageWrapper: LanguageWrapper

        fun init(parentLayout: LinearLayout, model: WidgetModel, values: ColData.WidgetValue) = apply {
            layout = LayoutInflater.from(context).inflate(R.layout.layout_profile_info_text, parentLayout, false) as ConstraintLayout
            txtlabel = layout.findViewById(R.id.txt_label)
            txtTitle = layout.findViewById(R.id.txt_selection)

            if(model.name.equals(FormConstants.DROP_DOWN)){
                var options: ArrayList<String>
                if(model.props.name.contains(FormConstants.LANGUAGES)){
                    options = ArrayList<String>(getOptions().values)
                }
                else{
                    options = ArrayList<String>(model.props.options.values)
                }

                val sb = StringBuilder()
                for (k in 0 until values.selectedIndex.size) {
                    if (k < values.selectedIndex.size - 1)
                        sb.append(options.get(values.selectedIndex.get(k))).append(",")
                    else
                        sb.append(options.get(values.selectedIndex.get(k)))
                }
                txtTitle.setText(sb.toString())
            }
            else{
                txtTitle.setText(values.value)
            }
            txtlabel.setText(model.props.labelText)
        }

        fun setLanguages(languageWrapper: LanguageWrapper) = apply {
            this.languageWrapper = languageWrapper
        }

        fun build() = layout


        fun getOptions(): Map<String, String>{
            val options = HashMap<String, String>()
            languageWrapper.success.forEach {
                options.put(it.id.toString(), it.name)
            }
            return options
        }
    }
}