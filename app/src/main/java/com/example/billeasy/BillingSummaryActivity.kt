package com.example.billeasy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class BillingSummaryActivity : AppCompatActivity() {
    private lateinit var customerName:TextView
    private lateinit var totalItems:TextView
    private lateinit var totalPrice:TextView
    private lateinit var orderDbHelper: ItemDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing_summary)

        customerName = findViewById(R.id.customer_name)
        totalItems = findViewById(R.id.total_items)
        totalPrice = findViewById(R.id.total_price)

        customerName.text = intent.getStringExtra("customerName")
        totalItems.text = intent.getIntExtra("totalItem",0).toString()
        totalPrice.text = intent.getFloatExtra("totalPrice",0F).toString()

        orderDbHelper = ItemDbHelper(this)

    }

    fun goToHomePage(view: View) {
        val i = Intent(this,MainActivity::class.java)
        startActivity(i)
    }

    fun confirmOrder(view: View) {
        Toast.makeText(this,"This is Confirm Order",Toast.LENGTH_LONG)
        try {
            orderDbHelper.insertDataIntoOrdersTable(
                intent.getIntExtra("totalItem", 0),
                intent.getFloatExtra("totalPrice", 0F),
                intent.getStringExtra("customerName"),
                intent.getStringExtra("customerPhone")
            )
            Toast.makeText(applicationContext, "Order confirmed", Toast.LENGTH_LONG)
        }catch (e:Exception){
            Toast.makeText(this, "Error : ${e.message.toString()}", Toast.LENGTH_LONG)
        }
        val i = Intent(this,HomePageActivity::class.java)
        startActivity(i)
    }

    fun goBackToBillingPage(view: View) {
        val i = Intent(this,BillingActivity::class.java)
        i.putExtra("name",intent.getStringExtra("customerName"))
        i.putExtra("phone",intent.getStringExtra("customerPhone"))
        startActivity(i)
    }
}