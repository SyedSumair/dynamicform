package com.sumair.dynamicform.utilities.customviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sumair.dynamicform.auth.form.http.wrapper.form.ColData
import com.sumair.dynamicform.auth.form.http.wrapper.form.WidgetModel
import com.sumair.dynamicform.R
import kotlinx.android.synthetic.main.item_bottom_dropdown.view.*


class MyBottomSheet {

    interface HandleClick{
        fun onItemClick(key: String, value: String, index: ArrayList<Int>)
    }

    class Builder(val context: Context) : HandleClick{
        lateinit var sheetDialog: BottomSheetDialog
        lateinit var layout: ConstraintLayout
        lateinit var displayTxt: TextView
        var recyclerview: RecyclerView? = null
        lateinit var adapter: BottomSheetAdapter
        var indices: ArrayList<Int> = ArrayList()
        var cancelIndices: ArrayList<Int> = ArrayList()
        var isClickable: Boolean = true

        fun init(parentLayout: LinearLayout, model: WidgetModel, values: ColData.WidgetValue) = apply {
            layout = LayoutInflater.from(context).inflate(R.layout.layout_dropdown, parentLayout, false) as ConstraintLayout
            /*displayTxt = TextView(context)
            displayTxt.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 60)
            displayTxt.id = values.view_id*/
            layout.id = values.view_id

            if(values.selectedIndex.size != 0) {
                indices.addAll(values.selectedIndex)
                cancelIndices.addAll(values.selectedIndex)
                layout.setTag(indices)
                //displayTxt.setTag(indices)
            }

            val v = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dropdown, null)
            sheetDialog = BottomSheetDialog(context)
            sheetDialog.setContentView(v)


            recyclerview = v.findViewById<RecyclerView>(R.id.recycler_bottom_sheet)
            adapter = BottomSheetAdapter(context, model.props.options, this, indices, model.props.isMultiple)
            recyclerview?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)


            model.props.labelText.let {
                layout.findViewById<TextView>(R.id.txt_label)?.setText(model.props.labelText + if(model.props.isRequired) context.getString(R.string.required_starric) else "")
                sheetDialog.findViewById<TextView>(R.id.txt_title)?.setText(model.props.labelText)

                val selectAnyOneOptional = context.getString(R.string.select_any_one, context.getString(R.string.optional_bracket))
                val selectAnyOne = context.getString(R.string.select_any_one, "")
                val selectMultipleOptional = context.getString(R.string.select_one_or_multiple, context.getString(R.string.optional_bracket))
                val selectMultiple = context.getString(R.string.select_one_or_multiple, "")

                if(!model.props.isMultiple) {
                    if(!model.props.isRequired) {
                        layout.findViewById<TextView>(R.id.txt_selection).setText(selectAnyOneOptional)
                        v.findViewById<TextView>(R.id.txt_label)?.setText(selectAnyOneOptional)
                    }
                    else {
                        layout.findViewById<TextView>(R.id.txt_selection).setText(selectAnyOne)
                        sheetDialog.findViewById<TextView>(R.id.txt_label)?.setText(selectAnyOne)
                    }
                }
                else {
                    if(!model.props.isRequired) {
                        layout.findViewById<TextView>(R.id.txt_selection).setText(selectMultipleOptional)
                        sheetDialog.findViewById<TextView>(R.id.txt_label)?.setText(selectMultipleOptional)
                    }
                    else {
                        layout.findViewById<TextView>(R.id.txt_selection).setText(selectMultiple)
                        sheetDialog.findViewById<TextView>(R.id.txt_label)?.setText(selectMultiple)
                    }
                }
            }

            if (indices.size != 0) {
                layout.findViewById<TextView>(R.id.txt_selection).setText(getLabelText(model))
            }



            sheetDialog.findViewById<Button>(R.id.btn_save)?.setOnClickListener{
                cancelIndices.clear()
                cancelIndices.addAll(indices)
                layout.setTag(indices)
                if (indices.size != 0) {
                    layout.findViewById<TextView>(R.id.txt_selection).setText(getLabelText(model))
                }
                sheetDialog.dismiss()
            }
            sheetDialog.findViewById<Button>(R.id.btn_close)?.setOnClickListener{
                indices.clear()
                indices.addAll(cancelIndices)
                layout.setTag(indices)
                sheetDialog.dismiss()
            }

            sheetDialog.setOnDismissListener{
                indices.clear()
                indices.addAll(cancelIndices)
                layout.setTag(indices)
                it.dismiss()
            }

            layout.setOnClickListener{
                this.recyclerview?.adapter = adapter
                sheetDialog.show()
            }

            if(!isClickable){
                layout.isEnabled = false
            }
        }

        fun setClickableLayout(b: Boolean) = apply{
            isClickable = b
        }

        fun getLabelText(widget: WidgetModel): String{
            //if(widget.props.isMultiple) {
                if (indices.size != 0) {
                    val options = ArrayList<String>(widget.props.options.values)
                    val sb = StringBuilder()
                    for (k in 0 until indices.size) {
                        if (k < indices.size - 1)
                            sb.append(options.get(indices.get(k))).append(",")
                        else
                            sb.append(options.get(indices.get(k)))
                    }

                    return sb.toString()
                }
            //}
            return ""
        }

        fun updateData(index: ArrayList<Int>){
            indices.clear()
            indices.addAll(index)
            adapter.setCurrentSelection(indices)
        }

        override fun onItemClick(key: String, value: String, index: ArrayList<Int>) {
            //layout.tag = index
            //displayTxt.tag = index
            adapter.setCurrentSelection(index)
            //sheetDialog.dismiss()
        }

        fun build() = layout
    }

    class BottomSheetAdapter(context: Context, map: Map<String, String>, listener: HandleClick, index: ArrayList<Int>, isMultiple: Boolean) : RecyclerView.Adapter<BottomSheetAdapter.MyViewHolder>(){
        var context: Context
        var keys: ArrayList<String>
        var options: ArrayList<String>
        var map: Map<String, String>
        var listener: HandleClick
        var indices: ArrayList<Int> = ArrayList()
        var isMultiple: Boolean

        init {
            this.context = context
            keys = ArrayList(map.keys)
            options = ArrayList(map.values)
            this.map = map;
            this.listener = listener
            indices = index
            this.isMultiple = isMultiple
        }

        fun setCurrentSelection(index: ArrayList<Int>){
            indices = index
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return options.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bottom_dropdown, parent, false))
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.txtItem.text = options.get(position)

            if(indices.contains(position)) holder.imgTickDropDown.visibility = View.VISIBLE
            else holder.imgTickDropDown.visibility = View.INVISIBLE

            holder.root.setOnClickListener {
                if(!isMultiple) {
                    if(indices.size != 0) indices.set(0, position)
                    else indices.add(position)
                }
                else{
                    if(indices.contains(position)) indices.remove(position)
                    else indices.add(position)
                }

                listener.onItemClick(keys.get(position), options.get(position), indices)
            }
        }


        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val root = itemView.root_item_dropdown
            val txtItem = itemView.txt_bottom_item
            val imgTickDropDown = itemView.img_tick_dropdown
        }

    }
}