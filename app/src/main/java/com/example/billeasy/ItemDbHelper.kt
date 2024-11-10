package com.example.billeasy

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date

class ItemDbHelper(context:Context):SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object{
        const val DATABASE_NAME = "item_db"
        const val DATABASE_VERSION = 5
        const val TABLE_NAME = "item_table"
        const val COL_ID = "_id"
        const val COL_ITEM_NAME = "item_name"
        const val COL_PRICE = "price"

        const val ORDER_TABLE_NAME = "order_table"
        const val COL_TOTAL_ITEMS = "total_items"
        const val COL_TOTAL_PRICE = "total_price"
        const val COL_CUSTOMER_NAME = "customer_name"
        const val COL_CUSTOMER_PHONE = "customer_phone"
        const val COL_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
        CREATE TABLE IF NOT EXISTS $TABLE_NAME (
            $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_ITEM_NAME TEXT,
            $COL_PRICE REAL
        )
    """.trimIndent()
        db?.execSQL(createTable)
        Log.d("DBHelper", "Item table created")

        val orderTableCreateTable = """
        CREATE TABLE IF NOT EXISTS $ORDER_TABLE_NAME (
            $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_TOTAL_ITEMS INTEGER,
            $COL_TOTAL_PRICE REAL,
            $COL_CUSTOMER_NAME TEXT,
            $COL_CUSTOMER_PHONE TEXT,
            $COL_DATE TEXT
        )
    """.trimIndent()
        db?.execSQL(orderTableCreateTable)
        Log.d("DBHelper", "Order table created")
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $ORDER_TABLE_NAME")
        onCreate(db)
    }


    fun insertData(id:Int,itemName:String,price:Float):Long
    {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COL_ID,id)
        values.put(COL_ITEM_NAME,itemName)
        values.put(COL_PRICE,price)
        return db.insert(TABLE_NAME,null,values)
    }

    fun findItemById(id:Int):ItemDataModel?{
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_ID = $id"
        val cursor = db.rawQuery(selectQuery,null)
        val count = cursor.count
        if(count<1)
        {
            return null
        }
        cursor.moveToFirst()
        val itemName = cursor.getString(1)
        val price = cursor.getFloat(2)
        return ItemDataModel(itemName,price,1F)
    }

    fun updateItemById(id:Int,itemName:String,price:Float){
        val db = writableDatabase
        val values = ContentValues()
        values.put(COL_ITEM_NAME,itemName)
        values.put(COL_PRICE,price)

        db.update(TABLE_NAME,values, "$COL_ID=?", arrayOf(id.toString()))
    }

    fun insertDataIntoOrdersTable(totalItems:Int,price:Float,customerName:String?,customerPhone:String?):Long
    {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val db = writableDatabase
        val values = ContentValues()
//        values.put(COL_ID,id)
        values.put(COL_TOTAL_ITEMS,totalItems)
        values.put(COL_TOTAL_PRICE,price)
        values.put(COL_CUSTOMER_NAME,customerName)
        values.put(COL_CUSTOMER_PHONE,customerPhone)
        values.put(COL_DATE,currentDate.toString())
        return db.insert(ORDER_TABLE_NAME,null,values)
    }

    fun getTodaysSell():Float{
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())
        val db = readableDatabase
        val selectQuery = "SELECT SUM($COL_TOTAL_PRICE) FROM $ORDER_TABLE_NAME WHERE $COL_DATE like '%$currentDate%'"
        val cursor = db.rawQuery(selectQuery, null)
        val count = cursor.count
        if(count<1)
            return 0F
        cursor.moveToFirst()
        return cursor.getFloat(0)
    }

    fun findOrders(): MutableList<OrdersModel> {
        val db = readableDatabase
        val selectQuery = "SELECT $COL_CUSTOMER_NAME, $COL_CUSTOMER_PHONE, $COL_TOTAL_PRICE FROM $ORDER_TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)

        val orderList: MutableList<OrdersModel> = mutableListOf()

        if (cursor.moveToFirst()) { // Check if the cursor has at least one row
            do {
                // Safely get values from the cursor, defaulting to empty strings or 0.0F if null
                val customerName = cursor.getString(0) ?: ""
                val customerPhone = cursor.getString(1) ?: ""
                val totalPrice = cursor.getFloat(2)

                // Add the OrdersModel to the list
                val ordersModel = OrdersModel(customerName, customerPhone, totalPrice)
                orderList.add(ordersModel)
            } while (cursor.moveToNext())
        }
        cursor.close() // Close the cursor to release resources
        return orderList
    }

    fun findTodayOrders(): MutableList<OrdersModel> {
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())
        val db = readableDatabase
        val selectQuery = "SELECT $COL_CUSTOMER_NAME, $COL_CUSTOMER_PHONE, $COL_TOTAL_PRICE FROM $ORDER_TABLE_NAME WHERE $COL_DATE like '%$currentDate%'"
        val cursor = db.rawQuery(selectQuery, null)

        val orderList: MutableList<OrdersModel> = mutableListOf()

        if (cursor.moveToFirst()) { // Check if the cursor has at least one row
            do {
                // Safely get values from the cursor, defaulting to empty strings or 0.0F if null
                val customerName = cursor.getString(0) ?: "-"
                val customerPhone = cursor.getString(1) ?: "-"
                val totalPrice = cursor.getFloat(2)

                // Add the OrdersModel to the list
                val ordersModel = OrdersModel(customerName, customerPhone, totalPrice)
                orderList.add(ordersModel)
            } while (cursor.moveToNext())
        }
        cursor.close() // Close the cursor to release resources
        return orderList
    }

}