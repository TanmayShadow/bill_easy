package com.example.billeasy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    lateinit var phoneNumber:EditText;
    lateinit var name:EditText;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        phoneNumber = findViewById(R.id.phone)
        name = findViewById(R.id.name)
    }

    fun goToNextBillingPage(view: View) {
        val intent = Intent(this,BillingActivity::class.java);
        intent.putExtra("phone",phoneNumber.text.toString());
        intent.putExtra("name",name.text.toString());
        startActivity(intent);
    }

    fun goToHomeScreen(view: View) {
        val intent = Intent(this,HomePageActivity::class.java);
        startActivity(intent);
    }
}