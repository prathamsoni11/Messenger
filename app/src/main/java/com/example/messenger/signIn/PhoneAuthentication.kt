package com.example.messenger.signIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.messenger.R
import kotlinx.android.synthetic.main.activity_phone_authentication.*

class PhoneAuthentication : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_authentication)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        addNumber.setOnClickListener {
            val str = phoneNumber.text.toString()
            if (str.trim().isNotEmpty() && str.length == 10) {
                val otpActivity = Intent(this, OTPActivity::class.java)
                otpActivity.putExtra("phoneNumber",str)
                startActivity(otpActivity)
                finish()
            }else{
                Toast.makeText(this,"Enter a valid number",Toast.LENGTH_SHORT).show()
            }
        }
    }
}