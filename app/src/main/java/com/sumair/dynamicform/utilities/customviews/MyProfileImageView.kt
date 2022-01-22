package com.sumair.dynamicform.utilities.customviews

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.sumair.dynamicform.auth.form.http.wrapper.form.ColData
import com.sumair.dynamicform.auth.form.http.wrapper.form.WidgetModel
import com.sumair.dynamicform.R

class MyProfileImageView {

    class Builder(val context: Context) {
        lateinit var layout: LinearLayout
        lateinit var imageView: AppCompatImageView
        lateinit var imageDelete: AppCompatImageView
        var isClickable: Boolean = true

        fun init(parentLayout: LinearLayout, model: WidgetModel, values: ColData.WidgetValue) = apply {
            layout = LayoutInflater.from(context).inflate(R.layout.layout_profile_image, parentLayout, false) as LinearLayout
            imageView = layout.findViewById(R.id.img_profileImage)
            imageDelete = layout.findViewById(R.id.img_delete)

            if(!model.props.isRequired) layout.findViewById<TextView>(R.id.txt_label).setText(context.getString(R.string.profile_image, context.getString(R.string.optional_bracket)))
            else layout.findViewById<TextView>(R.id.txt_label).setText(context.getString(R.string.profile_image, context.getString(R.string.required_starric)))

            if (!TextUtils.isEmpty(values.profileImage)) {
                //imageView.setImageURI(Uri.parse())
                Glide.with(context.applicationContext).load(values.profileImage).into(imageView)
                imageDelete.setVisibility(View.VISIBLE)
                imageView.setTag(R.string.imageview_tagid, values.profileImage)
            }
            else imageView.setImageResource(R.drawable.ic_launcher_background)

            layout.id = values.view_id

            imageDelete.setOnClickListener {
                imageView.setTag(null)
                imageView.setImageResource(R.drawable.ic_launcher_background)
                imageDelete.visibility = View.INVISIBLE
            }

            if(!isClickable){
                imageView.isEnabled = false
                layout.isEnabled = false
                imageDelete.setVisibility(View.GONE)
            }
        }

        fun setClickableLayout(b: Boolean) = apply{
            isClickable = b
        }

        fun build() = layout
    }
}