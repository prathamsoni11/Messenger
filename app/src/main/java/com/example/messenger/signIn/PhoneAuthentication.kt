package com.example.messenger.signIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.messenger.MainActivity
import com.example.messenger.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_phone_authentication.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class PhoneAuthentication : AppCompatActivity() {

    private lateinit var storedVerificationId: String
    private lateinit var auth: FirebaseAuth
    private var result: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_phone_authentication)

        val sp = getSharedPreferences("LogIn", MODE_PRIVATE)
        result = sp.getBoolean("LogInStatus",false)

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        if (result){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun submit(view: View) {
        if (phoneText.isEnabled) {
            val str = phoneNumber.text.toString()
            if (str.trim().isNotEmpty() && str.length == 10) {

                phoneText.isEnabled = false
                loading.visibility = View.VISIBLE
                submit.isEnabled = false

                submit.text = "Verify"

                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber("+91$str")       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(this)                 // Activity (for callback binding)
                    .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                        }

                        override fun onVerificationFailed(e: FirebaseException) {

                        }

                        override fun onCodeSent(
                            verificationId: String,
                            token: PhoneAuthProvider.ForceResendingToken
                        ) {

                            loading.visibility = View.GONE
                            otpText.visibility = View.VISIBLE
                            submit.isEnabled = true

                            storedVerificationId = verificationId
                        }
                    })          // OnVerificationStateChangedCallbacks
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)

            } else {

                Toast.makeText(this, "Enter a valid number", Toast.LENGTH_SHORT).show()

            }
        }else{
            
            otpText.isEnabled = false
            loading.visibility = View.VISIBLE
            submit.isEnabled = false

            val credential = PhoneAuthProvider.getCredential(
                storedVerificationId,
                otp.text.toString()
            )
            signInWithPhoneAuthCredential(credential)
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        GlobalScope.launch(Dispatchers.IO) {
            auth.signInWithCredential(credential).await()
            withContext(Dispatchers.Main){
                loading.visibility = View.GONE
                val profile = Intent(this@PhoneAuthentication,Profile::class.java)
                startActivity(profile)
                finish()
            }
        }
    }
}