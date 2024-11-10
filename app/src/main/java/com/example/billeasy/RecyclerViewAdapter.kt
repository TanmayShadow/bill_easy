package com.example.billeasy

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(
    private var itemList: MutableList<ItemDataModel>
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    lateinit var updateItemList: (String,Float,Float, Int) -> Unit

    constructor(list: MutableList<ItemDataModel>,
                updateItemList: (String,Float,Float, Int) -> Unit)
    : this(list) {
        this.updateItemList = updateItemList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_billing_view, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val position = holder.adapterPosition;
        val item = itemList[position]

        // Bind data to views
        holder.item_name.setText(item.itemName)
        holder.item_price.setText(item.price.toString())
        holder.item_quantity.setText(item.quantity.toString())

        // Handle delete icon click
        holder.delete_image.setOnClickListener {
            itemList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemList.size)
        }

        holder.item_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used, but required to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Update the item in the list when the text changes
//                itemList[position] = ItemDataModel(s.toString(), item.price, item.quantity)
//                updateItemList(s.toString(),item.price, item.quantity,position)
            }

            override fun afterTextChanged(s: Editable?) {
                // Notify item changed after text change
                holder.save_image.visibility=View.VISIBLE

            }
        })

        holder.item_price.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used, but required to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Update the item in the list when the text changes
//                itemList[position] = ItemDataModel(item.itemName, s.toString().toFloatOrNull() ?: 0f, item.quantity)
//                updateItemList(item.itemName, s.toString().toFloatOrNull() ?: 0f, item.quantity,position)
            }

            override fun afterTextChanged(s: Editable?) {
                holder.save_image.visibility=View.VISIBLE
            }
        })

        holder.item_quantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used, but required to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Update the item in the list when the text changes
//                itemList[position] = ItemDataModel(item.itemName, item.price, s.toString().toFloatOrNull() ?: 0f)
//                updateItemList(item.itemName, item.price, s.toString().toFloatOrNull() ?: 0f,position)
            }

            override fun afterTextChanged(s: Editable?) {
                holder.save_image.visibility=View.VISIBLE
            }
        })

        holder.save_image.setOnClickListener{
            val newName = holder.item_name.text.toString()
            val newPrice = holder.item_price.text.toString().toFloatOrNull() ?: 0f
            Log.d("ItemPrice",holder.item_price.text.toString())
            val newQuantity = holder.item_quantity.text.toString().toFloatOrNull() ?: 0f
            itemList[position] = ItemDataModel(newName,newPrice,newQuantity)
            notifyItemChanged(position)
            holder.save_image.visibility=View.INVISIBLE
        }

    }


    override fun getItemCount(): Int = itemList.size
    fun updateDataSet() {
        val newItemList = itemList.toMutableList()
        itemList.clear()
        itemList.addAll(newItemList)
        notifyDataSetChanged()
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val delete_image: ImageView = itemView.findViewById(R.id.delete_image)
        val item_name: EditText = itemView.findViewById(R.id.item_name)
        val item_price: EditText = itemView.findViewById(R.id.item_price)
        val item_quantity: EditText = itemView.findViewById(R.id.item_quantity)
        val save_image : ImageView = itemView.findViewById(R.id.save_image)
    }
}
