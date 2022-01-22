package com.sumair.dynamicform.utilities.customviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sumair.dynamicform.auth.form.http.wrapper.form.ColData
import com.sumair.dynamicform.auth.form.http.wrapper.form.WidgetModel
import com.sumair.dynamicform.FormActivity
import com.sumair.dynamicform.R
import kotlinx.android.synthetic.main.item_attachment.view.*

class MyAttachmentView {

    interface HandleImages {
        fun onImages(listImages: ArrayList<String>)
    }

    class Builder(val context: Context) : HandleImages {
        lateinit var layout: LinearLayout
        lateinit var recyclerView: RecyclerView
        lateinit var adapter: AttachmentAdapter
        var isClickable: Boolean = true

        fun init(parentLayout: LinearLayout, model: WidgetModel, values: ColData.WidgetValue, listenerKey: String) = apply {
            layout = LayoutInflater.from(context).inflate(R.layout.layout_attachments, parentLayout, false) as LinearLayout
            recyclerView = layout.findViewById(R.id.recycler_attachments)
            recyclerView.id = values.view_id
            if (!model.props.isRequired) layout.findViewById<TextView>(R.id.txt_label).setText(context.getString(R.string.attachments, context.getString(R.string.optional_bracket)))
            else layout.findViewById<TextView>(R.id.txt_label).setText(context.getString(R.string.attachments) + context.getString(R.string.required_starric))
            //recyclerView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = AttachmentAdapter(context, model, listenerKey, values.attachments, isClickable)
            recyclerView.adapter = adapter

        }

        fun setClickableLayout(b: Boolean) = apply{
            isClickable = b
        }

        fun getHandleImagesListener(): HandleImages {
            return this
        }

        override fun onImages(listImages: ArrayList<String>) {
            adapter.setDataSet(listImages)
        }

        fun build() = layout
    }

    class AttachmentAdapter(context: Context, dropZoneModel: WidgetModel, listenerKey: String, images: ArrayList<String>, clickable: Boolean) : RecyclerView.Adapter<AttachmentAdapter.MyViewHolder>() {
        var context: Context
        var dropZoneModel: WidgetModel
        var listImages: ArrayList<String> = ArrayList()
        var fileLimit: Int
        var listenerKey: String
        var isClickable: Boolean

        init {
            this.context = context
            isClickable = clickable
            listImages.addAll(images)

            if(isClickable)
                listImages.add(0, "")

            this.dropZoneModel = dropZoneModel
            this.fileLimit = this.dropZoneModel.props.fileLimit
            this.listenerKey = listenerKey
        }

        fun setDataSet(images: ArrayList<String>) {
            for (image in images){
                listImages.add(image)
            }

            /*listImages.clear()
            listImages.addAll(images)
            listImages.add(0, MediaFile())*/
            notifyDataSetChanged()
        }

        fun getDataSet(): ArrayList<String> {
            return listImages
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_attachment, parent, false))
        }

        override fun getItemCount(): Int {
            if (listImages.size == 0) return 1
            else return listImages.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            if (position == 0 && isClickable) {
                holder.imgDelete.visibility = View.GONE
                Glide.with(context).load(android.R.drawable.ic_menu_add).into(holder.imgAdd)
                //holder.imgAdd.setImageResource(Glide.with(context).load(values.imageUrl.get(0)).into(imageView))
                holder.imgAdd.setOnClickListener {
                    if(isClickable) {
                        if (listImages.size - 1 != fileLimit) {
                            if (context is FormActivity && position == 0) {
                            // open filepicker
                            //(context as MainActivity).openPicker(this.listenerKey, null, fileLimit - (listImages.size - 1), false)
                            }

                        } else
                            Toast.makeText(context, context.getString(R.string.attachments_limit, "" + fileLimit), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (listImages.size != 0) {
                if(isClickable){
                    holder.imgDelete.visibility = View.VISIBLE
                }
                Glide.with(context).load(listImages.get(position)).into(holder.imgAdd)
                //holder.imgAdd.setImageURI(Uri.parse(listImages.get(position).path))
                holder.imgAdd.setOnClickListener { null }
                //Glide.with(context).load(listImages.get(position).path).into(holder.imgAdd)
                holder.imgDelete.setOnClickListener {
                    listImages.removeAt(position)
                    notifyDataSetChanged()
                }
            }
        }


        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imgAdd = itemView.img_add
            val imgDelete = itemView.img_delete
        }
    }
}