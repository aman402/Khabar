package com.example.khabar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private lateinit var btLogin: Button
    private lateinit var btSignup: Button
    private lateinit var emailVal: EditText
    private lateinit var passVal: EditText
    private lateinit var showPass: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btLogin = findViewById(R.id.btLogin)
        btSignup = findViewById(R.id.btSignup)
        emailVal = findViewById(R.id.emailVal)
        passVal = findViewById(R.id.passVal)
        showPass = findViewById(R.id.showPass)
    }

    fun showPassword(v: View)
    {
        if(showPass.isChecked) passVal.transformationMethod = HideReturnsTransformationMethod.getInstance()
        else passVal.transformationMethod = PasswordTransformationMethod.getInstance()
        passVal.setSelection(passVal.length());
    }
}