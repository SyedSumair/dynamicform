package com.sumair.dynamicform.utilities.customviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sumair.dynamicform.R
import kotlinx.android.synthetic.main.item_bottom_dropdown.view.*

class BottomSheetCustomAdapter(context: Context, map: Map<String, String>, listener: HandleClick, index: ArrayList<Int>, isMultiple: Boolean) : RecyclerView.Adapter<BottomSheetCustomAdapter.MyViewHolder>(){
    var context: Context
    var keys: ArrayList<String>
    var options: ArrayList<String>
    var listener: HandleClick
    var indices: ArrayList<Int> = ArrayList()
    var isMultiple: Boolean

    init {
        this.context = context
        keys = ArrayList(map.keys)
        options = ArrayList(map.values)
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
    interface HandleClick{
        fun onItemClick(key: String, value: String, index: ArrayList<Int>)
    }
}