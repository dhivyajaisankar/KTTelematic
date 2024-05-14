package com.example.kttelematic

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {

    private lateinit var realm: Realm
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var login_Button: Button
    private lateinit var signUpButton: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Realm.init(this)
        realm = Realm.getDefaultInstance()

        usernameEditText = findViewById(R.id.Name_EditText)
        passwordEditText = findViewById(R.id.password_EditText)
        login_Button = findViewById(R.id.login_Button)
        signUpButton = findViewById(R.id.register_Button)

        login_Button.setOnClickListener {



            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Loading...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Check if user exists and password matches

            val user = realm.where<User>().equalTo("username", username).findFirst()

            if (user != null && user.password == password) {
                progressDialog.dismiss()

                val serviceIntent = Intent(this, LocationUpdatesService::class.java)
                startService(serviceIntent)

                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            } else {
                // Login failed, show error message
            }
        }

        signUpButton.setOnClickListener {

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)


        }
    }


    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}