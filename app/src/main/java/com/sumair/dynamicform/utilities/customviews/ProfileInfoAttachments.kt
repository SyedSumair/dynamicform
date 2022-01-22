package com.sumair.dynamicform.utilities.customviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sumair.dynamicform.R
import com.sumair.dynamicform.auth.form.http.wrapper.form.ColData
import com.sumair.dynamicform.auth.form.http.wrapper.form.WidgetModel
import kotlinx.android.synthetic.main.item_attachment.view.img_add
import kotlinx.android.synthetic.main.item_profile_attachment.view.*

class ProfileInfoAttachments {

    class Builder(val context: Context){
        lateinit var layout: LinearLayout
        lateinit var recyclerView: RecyclerView
        lateinit var txtlabel: TextView
        lateinit var adapter: AttachmentAdapter

        fun init(parentLayout: LinearLayout, model: WidgetModel, values: ColData.WidgetValue) = apply {
            layout = LayoutInflater.from(context).inflate(R.layout.layout_profile_info_attachments, parentLayout, false) as LinearLayout
            recyclerView = layout.findViewById(R.id.recycler_attachments)
            txtlabel = layout.findViewById(R.id.txt_label)
            txtlabel.setText(context.getString(R.string.attachments, ""))
            recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = AttachmentAdapter(context, values.attachments, values.attachmentNames)
            recyclerView.adapter = adapter
        }

        fun build() = layout
    }

    class AttachmentAdapter(context: Context, images: ArrayList<String>, names: ArrayList<String>) : RecyclerView.Adapter<AttachmentAdapter.MyViewHolder>() {
        var context: Context
        var listImages: ArrayList<String> = ArrayList()
        var listNames: ArrayList<String> = ArrayList()

        init {
            this.context = context
            listImages.addAll(images)
            listNames.addAll(names)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_profile_attachment, parent, false))
        }

        override fun getItemCount(): Int {
            return listImages.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val types = listImages.get(position).split(".")
            if(types.contains("jpeg") || types.contains("jpg") || types.contains("png")){
                Glide.with(context).load(listImages.get(position)).into(holder.imgAdd)
            }
            else{
                Glide.with(context).load(R.drawable.ic_launcher_background).into(holder.imgAdd)
            }
            holder.imgName.setText(listNames.get(position))
        }


        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imgAdd = itemView.img_add
            val imgName = itemView.img_name
        }
    }
}