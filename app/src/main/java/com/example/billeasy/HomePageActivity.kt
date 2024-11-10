package com.example.billeasy

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomePageActivity : AppCompatActivity() {
    lateinit var homePageRecyclerView:RecyclerView
    lateinit var recyclerViewAdapter: HomePageRecyclerViewAdapter
    lateinit var todaysSell:TextView
    lateinit var orderList:MutableList<OrdersModel>
    lateinit var todayOrderList:MutableList<OrdersModel>
    lateinit var allOrderList:MutableList<OrdersModel>
    lateinit var dbHelper: ItemDbHelper

    lateinit var todayButton:AppCompatButton
    lateinit var allButton:AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        dbHelper = ItemDbHelper(this)
        homePageRecyclerView = findViewById(R.id.home_page_recycler_view)
        todaysSell = findViewById(R.id.todays_sell)
        homePageRecyclerView.layoutManager = LinearLayoutManager(this)
        todaysSell.text = dbHelper.getTodaysSell().toString()

        orderList = dbHelper.findTodayOrders()
        recyclerViewAdapter = HomePageRecyclerViewAdapter(orderList)
        homePageRecyclerView.adapter = recyclerViewAdapter

        todayOrderList = dbHelper.findTodayOrders()
        allOrderList = dbHelper.findOrders()

        todayButton = findViewById(R.id.todayButton)
        allButton = findViewById(R.id.allButton)
    }

    fun selectTodaySell(view: View) {
        todayButton.setBackgroundResource(R.drawable.button_background)
        todayButton.setTextColor(Color.WHITE)
        allButton.setBackgroundResource(R.drawable.button_background_disable)
        allButton.setTextColor(Color.BLACK)
        orderList.clear()  // Clear the existing data
        orderList.addAll(todayOrderList)  // Add the new data
        recyclerViewAdapter.notifyDataSetChanged()
    }
    fun selectAllSell(view: View) {
        todayButton.setBackgroundResource(R.drawable.button_background_disable)
        todayButton.setTextColor(Color.BLACK)
        allButton.setBackgroundResource(R.drawable.button_background)
        allButton.setTextColor(Color.WHITE)
        orderList.clear()  // Clear the existing data
        orderList.addAll(allOrderList)  // Add the new data
        recyclerViewAdapter.notifyDataSetChanged()
    }

    fun goToAddCustomerPage(view: View) {
        val i: Intent = Intent(this,MainActivity::class.java)
        startActivity(i)
    }
}