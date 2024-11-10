package com.example.billeasy

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView

class HomePageRecyclerViewAdapter( private var orderList: MutableList<OrdersModel>) :
    RecyclerView.Adapter<HomePageRecyclerViewAdapter.ViewHolder>() {

    val letterColors = mapOf(
        'A' to "#FF5733",  // Bright red-orange
        'B' to "#33FF57",  // Bright lime green
        'C' to "#3357FF",  // Medium blue
        'D' to "#FF33A5",  // Hot pink
        'E' to "#33FFA5",  // Minty green
        'F' to "#A533FF",  // Vivid purple
        'G' to "#FFD633",  // Golden yellow
        'H' to "#33FFD6",  // Aqua or teal
        'I' to "#FF3380",  // Rose pink
        'J' to "#80FF33",  // Light lime green
        'K' to "#FF8C33",  // Bright pumpkin orange
        'L' to "#3380FF",  // Sky blue
        'M' to "#FF33D6",  // Fuchsia
        'N' to "#33FF8C",  // Soft emerald green
        'O' to "#8C33FF",  // Lavender purple
        'P' to "#FF5E33",  // Coral orange
        'Q' to "#33FF5E",  // Neon green
        'R' to "#5E33FF",  // Deep blue-violet
        'S' to "#FF3380",  // Bright raspberry pink
        'T' to "#33D6FF",  // Bright cyan
        'U' to "#D6FF33",  // Lemon-lime yellow
        'V' to "#FF3333",  // Bright red
        'W' to "#33FF33",  // Bright green
        'X' to "#3333FF",  // Cobalt blue
        'Y' to "#FF33B2",  // Magenta pink
        'Z' to "#33FFB2",   // Light teal
        '-' to "#000000"   // Light teal
    )


    class ViewHolder(orderView: View) : RecyclerView.ViewHolder(orderView) {
        val user_letter: AppCompatTextView = orderView.findViewById(R.id.user_letter)
        val order_customer_name: TextView = orderView.findViewById(R.id.order_customer_name)
        val order_customer_phone: TextView = orderView.findViewById(R.id.order_customer_phone)
        val order_price: TextView = orderView.findViewById(R.id.order_price)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomePageRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_detail_template, parent, false)
        return HomePageRecyclerViewAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomePageRecyclerViewAdapter.ViewHolder, position: Int) {
        if(orderList[position].customerName.isEmpty())
            return
        val userFirstCharacter = orderList[position].customerName[0].toString().uppercase()
        holder.user_letter.text= userFirstCharacter
        // Parse the color from your array
        val color = Color.parseColor(letterColors[userFirstCharacter[0]])

// Set the background tint
        holder.user_letter.backgroundTintList = ColorStateList.valueOf(color)


        holder.order_customer_name.text = orderList[position].customerName
        holder.order_customer_phone.text = orderList[position].customerPhone
        holder.order_price.text = orderList[position].price.toString()

    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}