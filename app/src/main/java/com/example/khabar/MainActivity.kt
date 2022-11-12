package com.example.khabar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var btLogin: Button
    private lateinit var btSignup: Button
    private lateinit var emailVal: EditText
    private lateinit var passVal: EditText
    private lateinit var showPass: CheckBox

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btLogin = findViewById(R.id.btLogin)
        btSignup = findViewById(R.id.btSignup)
        emailVal = findViewById(R.id.emailVal)
        passVal = findViewById(R.id.passVal)
        showPass = findViewById(R.id.showPass)

        auth = FirebaseAuth.getInstance()
        auth.signOut() // because android app saves the state of auth when we close the app so
                       // we will first sign out when we open the MainActivity

        showPass.setOnClickListener {
            if(showPass.isChecked) passVal.transformationMethod = HideReturnsTransformationMethod.getInstance()
            else passVal.transformationMethod = PasswordTransformationMethod.getInstance()
            passVal.setSelection(passVal.length())
        }

        btSignup.setOnClickListener {
            registerUser()
        }
        btLogin.setOnClickListener{
            loginUser()
        }
    }

    private fun registerUser() {
        val email = emailVal.text.toString()
        val pass = passVal.text.toString()
        if(email.isNotEmpty() && pass.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, pass).await()
                    if(checkLoggedInState()) {
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@MainActivity, "Successfully Registered", Toast.LENGTH_SHORT).show()
                        }
                        auth.signOut()
                    }
                } catch(e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun loginUser() {
        val email = emailVal.text.toString()
        val pass = passVal.text.toString()
        if(email.isNotEmpty() && pass.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, pass).await()
                    if(checkLoggedInState()) {
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@MainActivity, "Successfully Logged In", Toast.LENGTH_SHORT).show()
                            val myIntent = Intent(this@MainActivity, Home::class.java)
                            startActivity(myIntent)
                        }
                    }
                } catch(e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun checkLoggedInState(): Boolean {
        return (auth.currentUser != null)
    }

    override fun onResume() {
        super.onResume()
        // Toast.makeText(this@MainActivity, "Activity resumed", Toast.LENGTH_LONG).show()
    }
}