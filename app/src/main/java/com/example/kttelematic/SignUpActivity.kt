package com.example.kttelematic

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : ComponentActivity() {

    private lateinit var realm: Realm
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var mobilenoEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var login_Button: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        realm = Realm.getDefaultInstance()

        val config = RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(config)

        usernameEditText = findViewById(R.id.Name_EditText)
        passwordEditText = findViewById(R.id.password_EditText)
        mobilenoEditText = findViewById(R.id.Mobile_EditText)
        signUpButton = findViewById(R.id.Register_Button)
        login_Button = findViewById(R.id.login_Button)


        signUpButton.setOnClickListener {
//            val username = usernameEditText.text.toString()
//            val password = passwordEditText.text.toString()
//            val mobileno = mobilenoEditText.text.toString()
//
//            GlobalScope.launch(Dispatchers.Main) {
//                // Switch to a background thread (Dispatchers.IO) for Realm operations
//                withContext(Dispatchers.IO) {
//                    // Open a Realm instance within the background thread
//                    Realm.getDefaultInstance().use { realm ->
//                        realm.executeTransaction { realm ->
//                            // Create a new User object and set its properties
//                            val user = realm.createObject(User::class.java)
//                            user.username = username
//                            user.password = password
//                            user.mobileno = mobileno
//                        }
//                    }
//                }
//            }

            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Loading...")
            progressDialog.setCancelable(false)
            progressDialog.show()




            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val mobileno = mobilenoEditText.text.toString()

            // Check if the username already exists
            val existingUser = realm.where<User>().equalTo("username", username).findFirst()
            if (existingUser != null) {
                return@setOnClickListener
               progressDialog.dismiss()
                showSimplePopup("OOPS!!", "User Already Exist")
                Toast.makeText(this,"User Already Exist",Toast.LENGTH_SHORT).show()
            }

            realm.executeTransactionAsync { bgRealm ->
                val newUser = bgRealm.createObject<User>(username)
                newUser.password = password
            }
            progressDialog.dismiss()
            showSimplePopup("HURRAY!!", "User Created Successfully")
            Toast.makeText(this,"User Created Successfully",Toast.LENGTH_SHORT).show()

        }

        login_Button.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun showSimplePopup(title: String, message: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}