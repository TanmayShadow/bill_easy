package com.example.billeasy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class BillingActivity : AppCompatActivity() {
    val itemList : MutableList<ItemDataModel> = mutableListOf<ItemDataModel>()
    lateinit var recyclerview:RecyclerView
    lateinit var adapter:RecyclerViewAdapter
    lateinit var dbHelper: ItemDbHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing)
        val customerName = intent.getStringExtra("name").toString();
        val phone = intent.getStringExtra("phone").toString();
        val customerNameTextView:TextView = findViewById<TextView>(R.id.customer_name)
        customerNameTextView.text= customerName;
        recyclerview = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerview.layoutManager = LinearLayoutManager(this)
        dbHelper = ItemDbHelper(this)

    }

    fun addItem(view: View) {
        val scanOptions = ScanOptions()
        scanOptions.setPrompt("Volume up to flash on")
        scanOptions.setBeepEnabled(true)
        scanOptions.setOrientationLocked(true)
        scanOptions.setCaptureActivity(CaptureAct::class.java)
        barCodeLauncher.launch(scanOptions)

    }
    val barCodeLauncher = registerForActivityResult(ScanContract()) { result ->
        result.contents?.let { scannedContent ->
            // Use scannedContent as the output of the barcode scanner
            // For example, you could display it in a TextView or use it in other logic

            Toast.makeText(this, "Scanned: $scannedContent", Toast.LENGTH_LONG).show()
            this.addItemToList(scannedContent)
        } ?: run {
            Toast.makeText(this, "No result found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addItemToList(id: String) {
        val item:ItemDataModel? = dbHelper.findItemById(id.toInt())
        if(item!=null) {
            itemList.add(item)
            adapter = RecyclerViewAdapter(itemList, ::updateItemName)
            recyclerview.adapter = adapter
        }
        else{
            Toast.makeText(this,"Item Not found in DB please add this item",Toast.LENGTH_SHORT)
            val item = showAddItemPopUp(id.toInt())
            if(item!=null) {
                itemList.add(item)
                adapter = RecyclerViewAdapter(itemList, ::updateItemName)
                recyclerview.adapter = adapter
            }
        }
    }

    fun goBack(view: View) {
        val intent = Intent(this,MainActivity::class.java);
        startActivity(intent);
    }


    private fun updateItemName(name : String,price:Float,quantity:Float,position:Int):Unit{
        itemList[position] = ItemDataModel(name, price, quantity)
        Log.d("New Dataset",itemList.toString())
    }

    private fun showAddItemPopUp(id:Int):ItemDataModel?{
        val builder = AlertDialog.Builder(this)
        val inflater :LayoutInflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.add_item_popup,null)
        val newItemName = dialogLayout.findViewById<EditText>(R.id.new_item_name)
        val newItemPrice = dialogLayout.findViewById<EditText>(R.id.new_item_price)
        var itemData : ItemDataModel? = null
        with(builder){
            setTitle("Save Item")
            setPositiveButton("Save"){
                dialog,which->
                    itemData = ItemDataModel(newItemName.text.toString(),newItemPrice.text.toString().toFloat())
                    dbHelper.insertData(id,newItemName.text.toString(),newItemPrice.text.toString().toFloat())

            }
            setNegativeButton("Close"){
                dialog,which->
                    itemData = null
                    Log.d("Cancelled","cancelled")
            }
            setView(dialogLayout)
            show()
        }
        return itemData
    }

    fun goToBillingSummary(view: View) {
        var total:Float = 0f
        for(item:ItemDataModel in itemList){
            total+= (item.price*item.quantity)
        }
        val i : Intent = Intent(this,BillingSummaryActivity::class.java)
        i.putExtra("customerName",intent.getStringExtra("name").toString())
        i.putExtra("customerPhone",intent.getStringExtra("phone").toString())
        i.putExtra("totalItem",itemList.size)
        i.putExtra("totalPrice",total)
        startActivity(i)
    }

}