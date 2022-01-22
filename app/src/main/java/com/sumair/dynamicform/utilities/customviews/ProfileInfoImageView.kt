package com.sumair.dynamicform.utilities.customviews

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.sumair.dynamicform.R
import com.sumair.dynamicform.auth.form.http.wrapper.form.ColData
import com.sumair.dynamicform.auth.form.http.wrapper.form.WidgetModel

class ProfileInfoImageView {

    class Builder(val context: Context) {
        lateinit var layout: LinearLayout
        lateinit var imageView: AppCompatImageView
        lateinit var txtlabel: TextView

        fun init(parentLayout: LinearLayout, model: WidgetModel, values: ColData.WidgetValue) = apply {
            layout = LayoutInflater.from(context).inflate(R.layout.layout_profile_info_prof_image, parentLayout, false) as LinearLayout
            imageView = layout.findViewById(R.id.img_profileImage)
            txtlabel = layout.findViewById(R.id.txt_label)

            txtlabel.setText(context.getString(R.string.profile_image, ""))

            if (!TextUtils.isEmpty(values.profileImage)) {
                //imageView.setImageURI(Uri.parse())
                Glide.with(context).load(values.profileImage).into(imageView)
            }
            else imageView.setImageResource(R.drawable.ic_launcher_background)

        }

        fun build() = layout
    }
}