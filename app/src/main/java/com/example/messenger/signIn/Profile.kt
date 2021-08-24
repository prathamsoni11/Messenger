package com.example.messenger.signIn

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.messenger.MainActivity
import com.example.messenger.R
import com.example.messenger.dao.ProfileDao
import com.example.messenger.dao.UserDao
import com.example.messenger.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Profile : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private var selectedImage: Uri? = null
    private lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        dialog = ProgressDialog(this)
        dialog.setMessage("Completing Profile...")
        dialog.setCancelable(false)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        profile_image.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,45)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null && data.data != null){
            profile_image.setImageURI(data.data)
            selectedImage = data.data!!
        }
    }

    fun done(view: View) {
        val name = userName.text.toString()
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            return
        }

        userText.isEnabled = true
        completing.visibility = View.VISIBLE
        done.isEnabled = false

        GlobalScope.launch(Dispatchers.IO) {
            if (selectedImage != null) {

                val profileDao = ProfileDao()
                val imageUrl = profileDao.addProfilePicture(selectedImage!!)

                val uid = auth.uid.toString()
                val userPhone = auth.currentUser!!.phoneNumber.toString()

                val user = User(uid, name, userPhone, imageUrl.toString())

                val userDao = UserDao()
                userDao.addUser(user)

            }else{

                val uid = auth.uid.toString()
                val userPhone = auth.currentUser!!.phoneNumber.toString()

                val user = User(uid, name, userPhone, "No Image")

                val userDao = UserDao()
                userDao.addUser(user)
            }

            val sp = getSharedPreferences("LogIn", MODE_PRIVATE)
            val ed = sp.edit()
            ed.putBoolean("LogInStatus",true)
            ed.apply()

            withContext(Dispatchers.Main){
                completing.visibility = View.GONE
                val intent = Intent(this@Profile, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}